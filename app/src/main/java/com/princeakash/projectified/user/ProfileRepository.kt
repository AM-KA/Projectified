
import com.princeakash.projectified.user.BodyCreateProfile
import com.princeakash.projectified.user.BodyUpdateProfile
import com.princeakash.projectified.user.ProfileService
import retrofit2.Retrofit

//TODO:Show exception via toast.
class ProfileRepository(retrofit: Retrofit) {
    var profileService : ProfileService = retrofit.create(ProfileService::class.java)

    suspend fun createProfile(token: String,bodyCreateProfile: BodyCreateProfile)  = profileService.createProfile(token, bodyCreateProfile)

    suspend fun updateProfile(token: String,bodyUpdateProfile: BodyUpdateProfile) = profileService.updateProfile(token,bodyUpdateProfile)

}


