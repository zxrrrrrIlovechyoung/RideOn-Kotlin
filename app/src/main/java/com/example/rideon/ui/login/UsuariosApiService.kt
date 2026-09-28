package com.example.rideon.ui.login
import com.example.rideon.network.ForgotPasswordRequest
import com.example.rideon.network.ResetPasswordRequest
import com.example.rideon.network.ForgotPasswordResponse
import com.example.rideon.network.ResetPasswordResponse
import com.example.rideon.network.RegisterRequest
import com.example.rideon.network.RegisterResponse
import com.example.rideon.network.LoginRequest
import com.example.rideon.network.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import com.example.rideon.network.VerifyTokenRequest
import com.example.rideon.network.VerifyTokenResponse
import com.example.rideon.ui.login.registro.SexoResponse

interface UsuariosApiService {
    @POST("api/UsuariosApi/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/UsuariosApi/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("api/UsuariosApi/getSexos")
    suspend fun getSexos(): Response<List<SexoResponse>>

    @POST("api/UsuariosApi/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    @POST("api/UsuariosApi/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @POST("api/UsuariosApi/verifyToken")
    suspend fun verifyToken(@Body request: VerifyTokenRequest): Response<VerifyTokenResponse>

}

