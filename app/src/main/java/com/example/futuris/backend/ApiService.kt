package com.example.futuris.backend

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// ======================= AUTH =======================

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val gender: String,
    val username: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val message: String,
    val user: UserData? = null
)

data class UserData(
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val phone: String? = null,
    val insights: List<String> = emptyList(),
    val isVerified: Boolean? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: UserData? = null,
    val requiresVerification: Boolean? = null,
    val email: String? = null
)

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val message: String
)

data class VerifyEmailRequest(
    val email: String,
    val code: String
)

data class VerifyEmailResponse(
    val success: Boolean = false,
    val message: String = ""
)

data class ResendVerificationCodeRequest(
    val email: String
)

data class ResendVerificationCodeResponse(
    val message: String = ""
)

data class ChangePasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)

data class ChangePasswordResponse(
    val message: String = ""
)

// ======================= LIVE AI INSIGHT =======================

data class AiInsightRequest(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val gender: String,
    val dateOfBirth: String,
    val category: String,
    val lifeFocus: String? = null,
    val state: String? = null,
    val intent: String? = null,
    val zodiac: String,
    val quizAnswers: List<QuizAnswerPayload> = emptyList(),
    val chatMessages: List<String> = emptyList(),
    val localScores: Map<String, Int> = emptyMap(),
    val topKeywords: List<String> = emptyList(),
    val localPrediction: String = ""
)

data class QuizAnswerPayload(
    val questionId: String,
    val selectedOptionText: String
)

data class AiInsightResponse(
    val success: Boolean = false,
    val title: String = "",
    val insight: String = "",
    val questions: List<String> = emptyList(),
    val advice: String = "",
    val energy: String = "",
    val focus: String = "",
    val confidence: Int = 0,
    val source: String = "local",
    val message: String = ""
)

interface ApiService {

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>

    @POST("api/auth/verify-email")
    suspend fun verifyEmail(
        @Body request: VerifyEmailRequest
    ): Response<VerifyEmailResponse>

    @POST("api/auth/resend-verification-code")
    suspend fun resendVerificationCode(
        @Body request: ResendVerificationCodeRequest
    ): Response<ResendVerificationCodeResponse>

    @POST("api/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    @POST("api/insight/generate")
    suspend fun generateAiInsight(
        @Body request: AiInsightRequest
    ): Response<AiInsightResponse>
}