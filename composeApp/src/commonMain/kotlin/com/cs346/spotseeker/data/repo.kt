package com.cs346.spotseeker.data

import com.cs346.spotseeker.domain.Address
import com.cs346.spotseeker.domain.Photo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

class MySharedPhotoRepository(
   private val client: SupabaseClient
) : IPhotoRepository {

   @Serializable
   data class PhotoDto(
       val id: Long,        // Supabase int8 → Kotlin Long
       val url: String,
       val lat: Double,
       val lng: Double,
       val good: Int,
       val bad: Int
   )

   /** DTO → Domain */
   private fun toDomain(dto: PhotoDto): Photo {
       return Photo(
           photoPath = dto.url,
           address = Address(dto.lat, dto.lng),
           id = dto.id.toString(), // convert Long → String
           good = dto.good,
           bad = dto.bad
       )
   }

   /** Domain → DTO */
   private fun fromDomain(photo: Photo): PhotoDto {
       return PhotoDto(
           id = requireNumericId(photo.id),      // convert String → Long
           url = photo.photoPath,
           lat = photo.address.x,
           lng = photo.address.y,
           good = photo.good,
           bad = photo.bad
       )
   }

   override fun getAllPhotos(): Flow<List<Photo>> = flow {
       val dtos = client.from("photos").select().decodeList<PhotoDto>()
       emit(dtos.map { toDomain(it) })
   }

   override fun getNQuestionPhotos(n: Int): Flow<List<Photo>> {
       return getAllPhotos().map { list ->
           list.shuffled().take(n)
       }
   }

   override suspend fun addPhoto(photo: Photo) {
       val dto = fromDomain(photo)
       client.from("photos").insert(dto)
   }

   override suspend fun updatePhoto(photo: Photo) {
       val dto = fromDomain(photo)
       updateDto(dto)
   }

   override suspend fun getPhoto(id: String): Photo? {
       val dto = fetchDtoById(id)
       return dto?.let { toDomain(it) }
   }

   override suspend fun uploadPhoto(photo: Photo, data: ByteArray) {
       val storage = client.storage.from("images")

       // file path = "<id>.jpg"
       val filePath = "${photo.id}.jpg"

       // upload binary
       storage.upload(filePath, data, upsert = true)

       // get public URL
       val url = storage.publicUrl(filePath)

       // create new Photo with the URL
       val updated = photo.copy(photoPath = url)

       addPhoto(updated)
   }

   private fun requireNumericId(rawId: String): Long {
       return rawId.toLongOrNull()
           ?: throw IllegalArgumentException("MySharedPhotoRepository requires numeric id, got '$rawId'")
   }

   private suspend fun fetchDtoById(id: String): PhotoDto? {
       val numericId = id.toLongOrNull() ?: return null
       return client.from("photos").select {
           filter { eq("id", numericId) }
       }.decodeSingleOrNull<PhotoDto>()
   }

   private suspend fun updateDto(dto: PhotoDto) {
       client.from("photos").update(dto) {
           filter { eq("id", dto.id) }
       }
   }

   suspend fun voteGoodById(id: String): Photo? {
       val dto = fetchDtoById(id) ?: return null
       val updated = dto.copy(good = dto.good + 1)
       updateDto(updated)
       return toDomain(updated)
   }

   suspend fun voteBadById(id: String): Photo? {
       val dto = fetchDtoById(id) ?: return null
       val updated = dto.copy(bad = dto.bad + 1)
       updateDto(updated)
       return toDomain(updated)
   }
}

