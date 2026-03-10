package com.example.proyectosimagrow.api

import com.example.proyectosimagrow.data.Espacio
import com.example.proyectosimagrow.data.Incidencia
import com.example.proyectosimagrow.data.IncidenciaRequest
import com.example.proyectosimagrow.data.LoginRequest
import com.example.proyectosimagrow.data.RecompensaResponse
import com.example.proyectosimagrow.data.UserResponse
import com.example.proyectosimagrow.data.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SimagrowApiServices {

    @POST("users/loginApp")
    suspend fun login(@Body loginRequest: LoginRequest): Response<UserResponse>

    @GET("incidencies")
    suspend fun getIncidencias(): Response<List<Incidencia>>

    @GET("espais")
    suspend fun getEspacios(): Response<List<Espacio>>

    @GET("recompenses")
    suspend fun getRecompensas(): Response<List<RecompensaResponse>>

    @POST("incidencies")
    suspend fun crearIncidencia(@Body request: IncidenciaRequest): Response<Incidencia>

    @PUT("incidencies/{id}")
    suspend fun modificarIncidencia(
        @Path("id") id: Int,
        @Body request: IncidenciaRequest
    ): Response<Incidencia>

    @DELETE("incidencies/{id}")
    suspend fun eliminarIncidencia(@Path("id") id: Int): Response<Unit>

    @PUT("users/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body request: UsuarioRequest
    ): Response<UserResponse>

}