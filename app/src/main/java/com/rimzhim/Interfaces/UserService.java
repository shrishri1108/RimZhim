package com.rimzhim.Interfaces;

import com.rimzhim.ModelClasses.BlockUserResponseModel;
import com.rimzhim.ModelClasses.CMSPagesResponse;
import com.rimzhim.ModelClasses.ChatModel.ChatsInboxesResponse;
import com.rimzhim.ModelClasses.ContestResponse.ContestsResponseModel;
import com.rimzhim.ModelClasses.FollowUserResponseModel;
import com.rimzhim.ModelClasses.FollowerFollowingResponseModel.Following;
import com.rimzhim.ModelClasses.GetChats.getChatResponse;
import com.rimzhim.ModelClasses.JoinContestResponse.JoinContestResponse;
import com.rimzhim.ModelClasses.LeaderBordModel.LeaderboardResponse;
import com.rimzhim.ModelClasses.LoginRequest;
import com.rimzhim.ModelClasses.LoginResponse;

import com.rimzhim.ModelClasses.SimpleResponse;
import com.rimzhim.ModelClasses.StateModel.StateModel;
import com.rimzhim.ModelClasses.TransactionHistory.TransactionHistoryResponse;
import com.rimzhim.ModelClasses.UploadKYCResponseModel;
import com.rimzhim.ModelClasses.UserProfile.UserProfile;
import com.rimzhim.ModelClasses.likeVideoResponse;
import com.rimzhim.ModelClasses.model.UserProfileResponse;
import com.rimzhim.ModelClasses.optRequestModel;
import com.rimzhim.ModelClasses.optResponseModel;
import com.rimzhim.ModelClasses.submitChatResponse;
import com.rimzhim.ModelClasses.uploadVideoResponseModel;
import com.rimzhim.ModelClasses.verifyOtpModelRequest;
import com.rimzhim.ModelClasses.verifyOtpResponse;
import com.rimzhim.ModelClasses.videoListModel.videoList;
import com.rimzhim.ModelClasses.videoReportResponseModel;
import com.rimzhim.ModelClasses.voteVideoResponseModel;
import com.rimzhim.PaymentGateway.CreatePaymentResponse.CreatePaymentResponse;
import com.rimzhim.PaymentGateway.UpdatePaymentStatusReesponse.UpdatePaymentStatusResponse;

import java.util.HashMap;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {


   @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("profile")
    Call<UserProfile> userProfile(@Body HashMap<String, String> hashMap);

    @POST("register")
    Call<LoginResponse> userSignUp(@Body HashMap<String, String> hashMap);

      @POST("send_otp")
    Call<optResponseModel> userOtpOnMobile(@Body optRequestModel optRequestModel);

    @POST("verify_otp")
    Call<verifyOtpResponse> userVerifyOtp(@Body verifyOtpModelRequest verifyOtpModelRequest);

    @POST("state_city_list")
    Call<StateModel> LoadStateList();

    @POST("video_list")
    Call<videoList> LoadVideoList(@Body HashMap<String, String> hashMap);

    @POST("gallery_video_list")
    Call<videoList> GalleryVideoList(@Body HashMap<String, String> hashMap);

   @POST("update_profile")
    Call<LoginResponse> editProfile(@Body HashMap<String, String> hashMap);

    @POST("change_password")
    Call<LoginResponse> setNewPassword(@Body HashMap<String, String> hashMap);

    @Multipart
    @POST("update_profile")
    Call<LoginResponse> uploadImg(@Part("token") RequestBody token, @Part MultipartBody.Part image);

    @Multipart
    @POST("update_profile")
    Call<LoginResponse> uploadCoverImg(@Part("token") RequestBody token, @Part MultipartBody.Part image);


   /* @Multipart
    @POST("api/api/video_upload")
    Single<uploadVideoResponseModel> uploadVideo(@Part("token") RequestBody token, @Part MultipartBody.Part video);
*/


    @Multipart
    @POST("video_upload")
    Call<uploadVideoResponseModel> uploadVideo(@Part("token") RequestBody token, @Part("tags") RequestBody tags, @Part MultipartBody.Part video);


    @POST("following_list")
    Call<Following> following_list(@Body HashMap<String, String> hashMap);

   @POST("other_user_following_list")
   Call<Following> OtherUserFollowing_list(@Body HashMap<String, String> hashMap);


    @POST("user_profile")
    Call<UserProfileResponse> OpenOtherUserProfile(@Body HashMap<String, String> hashMap);

    @POST("like_video")
    Call<likeVideoResponse> likeVideo(@Body HashMap<String, String> hashMap);

    @POST("vote_video")
    Call<voteVideoResponseModel> voteVideo(@Body HashMap<String, String> hashMap);

    @POST("report_video")
    Call<videoReportResponseModel> reportVideo(@Body HashMap<String, String> hashMap);

    @POST("follow_user")
    Call<FollowUserResponseModel> FollowUser(@Body HashMap<String, String> hashMap);

    @POST("block_user")
    Call<BlockUserResponseModel> BlockUser(@Body HashMap<String, String> hashMap);


    @Multipart
    @POST("upload_kyc")
    Call<UploadKYCResponseModel> UploadKYC(@Part("token") RequestBody token, @Part("pan_no") RequestBody pan_no,
                                           @Part("type") RequestBody type, @Part MultipartBody.Part pan_card,
                                           @Part("name") RequestBody name, @Part("state") RequestBody state,
                                           @Part("dob") RequestBody dob);

    @Multipart
    @POST("upload_kyc")
    Call<UploadKYCResponseModel> UploadAadharKYC(@Part("token") RequestBody token, @Part("pan_no") RequestBody pan_no,
                                           @Part("type") RequestBody type, @Part MultipartBody.Part pan_card);

    @POST("leaderboard")
    Call<LeaderboardResponse> leaderboard(@Body HashMap<String, String> hashMap);

    @POST("contest_list")
    Call<ContestsResponseModel> ContestList(@Body HashMap<String, String> hashMap);

    @POST("chat_list")
    Call<ChatsInboxesResponse> ChatsList(@Body HashMap<String, String> hashMap);

    @POST("submit_chat")
    Call<submitChatResponse> submitChat(@Body HashMap<String, String> hashMap);

    @POST("get_chats")
    Call<getChatResponse> getChats(@Body HashMap<String, String> hashMap);



    @Multipart
    @POST("submit_chat")
    Call<submitChatResponse> sendImage(@Part("token") RequestBody token, @Part MultipartBody.Part text, @Part("reciever_id") RequestBody recieverID);

    @POST("create_payment")
    Call<CreatePaymentResponse> getOrderId(@Body HashMap<String, String> hashMap);

    @POST("update_payment_status")
    Call<UpdatePaymentStatusResponse> updatePaymentStatus(@Body HashMap<String, String> hashMap);

    @POST("join_contest")
    Call<JoinContestResponse> JoinContest(@Body HashMap<String, String> hashMap);

    @POST("delete_video")
    Call<SimpleResponse> DeleteVideo(@Body HashMap<String, String> hashMap);

    @POST("logout")
    Call<SimpleResponse> LogOut(@Body HashMap<String, String> hashMap);

    @POST("transaction_history")
    Call<TransactionHistoryResponse> getTransactionHistory(@Body HashMap<String, String> hashMap);

    @POST("withdrawal_request")
    Call<SimpleResponse> WithdrawalCashRequest(@Body HashMap<String, String> hashMap);

    @POST("account_deactivate")
    Call<SimpleResponse> DeleteAccount(@Body HashMap<String, String> hashMap);

   @POST("cmspages")
   Call<CMSPagesResponse> getCMSPages(@Body HashMap<String, String> hashMap);


}

