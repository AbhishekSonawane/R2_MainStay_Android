package com.android.main_stay.Classes;

/**
 * Created by nonstop on 23/10/17.
 */

public class R2Values {

    public static class Commons {
        public static String TOKEN ="device_token" ;
        public static String EMAIL = "email";
        public static String PASSWORD = "password";
        public static String ISUSER_LOGGEDIN = "user_loggedin";
        public static String STUDENT_ID = "student_id";
        public static String POST_ID = "post_id";
    }

    public static class Web {

       // public static String BASE_URL1 = "http://192.168.1.9:1212/";
      //  public static String BASE_URL = "https://3f91-103-35-134-125.in.ngrok.io/";
        public static String BASE_URL = "http://3.225.180.208:8080/";
      //  public static String BASE_URL = "https://0570-103-35-134-125.in.ngrok.io/" +
              //  "";

        public static class LoginService {

            public static String SERVICE_URL = BASE_URL + "login/";
            public static String DATA = "data";
        }

        public static class CreatePost {
            public static String SERVICE_URL = BASE_URL + "add_post/";
            public static String DATA = "data";
        }

        public static class GetPosts {
            public static String SERVICE_URL = BASE_URL + "get_posts/";
            public static String DATA = "data";
        }

        public static class LikePost {
            public static String SERVICE_URL = BASE_URL + "like_post/";
            public static String DATA = "data";
        }

        public static class AddComment {
            public static String SERVICE_URL = BASE_URL + "add_comment/";
            public static String DATA = "data";
        }

        public static class GetComments {
            public static String SERVICE_URL = BASE_URL + "get_comments/";
            public static String DATA = "data";
        }

        public static class ChangePassword {
            public static String SERVICE_URL = BASE_URL + "change_password/";
            public static String DATA = "data";
        }

        public static class ToggleBookmark {
            public static String SERVICE_URL = BASE_URL + "toggle_bookmark/";
            public static String DATA = "data";
        }

        public static class GetBookmark {
            public static String SERVICE_URL = BASE_URL + "get_bookmarks/";
            public static String DATA = "data";
        }

        public static class UpdateProfile {
            public static String SERVICE_URL = BASE_URL + "update_profile/";
            public static String DATA = "data";
        }

        public static class GetProfileInfo {
            public static String SERVICE_URL = BASE_URL + "get_profile_info/";
            public static String DATA = "data";
        }

        public static class GetBatchStudents {
            public static String SERVICE_URL = BASE_URL + "get_batch_students/";
            public static String DATA = "data";
        }

        public static class AddFeedback {
            public static String SERVICE_URL = BASE_URL + "add_feedback/";
            public static String DATA = "data";
        }

        public static class GetNotifications {
            public static String SERVICE_URL = BASE_URL + "get_notifications/";
            public static String DATA = "data";
        }

        public static class ReadNotification {
            public static String SERVICE_URL = BASE_URL + "read_notification/";
            public static String DATA = "data";
        }

        public static class SetVersion {
            public static String SERVICE_URL = BASE_URL + "set_version/";
            public static String DATA = "data";
        }

        public static class GetStudentQuizList {
            public static String SERVICE_URL = BASE_URL + "get_student_quiz_list/";
            public static String DATA = "data";
        }

        public static class GetDashboradCount {
            public static String SERVICE_URL = BASE_URL + "get_dashboard_count/";
            public static String DATA = "data";
        }

        public static class GetQuizQuestions {
            public static String SERVICE_URL = BASE_URL + "get_quiz_questions/";
            public static String DATA = "data";
        }

        public static class SubmitQuiz {
            public static String SERVICE_URL = BASE_URL + "submit_quiz/";
            public static String DATA = "data";
        }

        public static class ViewQuizAnswers {
            public static String SERVICE_URL = BASE_URL + "view_quiz_answers/";
            public static String DATA = "data";
        }

        public static class GetStudentAssignmentsList {
            public static String SERVICE_URL = BASE_URL + "get_student_assign_list/";
            public static String DATA = "data";
        }

        public static class GetAssignment {
            public static String SERVICE_URL = BASE_URL + "start_assignment/";
            public static String DATA = "data";
        }

        public static class SubmitAssignment {
            public static String SERVICE_URL = BASE_URL + "submit_assignment/";
            public static String DATA = "data";
        }

        public static class ViewAssignmentAnswer {
            public static String SERVICE_URL = BASE_URL + "view_assignment_ans/";
            public static String DATA = "data";
        }

        public static class GetApplicationList {
            public static String SERVICE_URL = BASE_URL + "get_application_list/";
            public static String DATA = "data";
        }

        public static class GetRankings {
            public static String SERVICE_URL = BASE_URL + "get_leader_board_ranks/";
            public static String DATA = "data";
        }

        public static class SubmitApplication {
            public static String SERVICE_URL = BASE_URL + "submit_application/";
            public static String DATA = "data";
        }

        public static class viewApplicationAnswer {
            public static String SERVICE_URL = BASE_URL + "view_app_answers/";
            public static String DATA = "data";
        }

        public static class logout {
            public static String SERVICE_URL = BASE_URL + "logout/";
            public static String DATA = "data";
        }

        public static class GetBadgeList {
            public static String SERVICE_URL = BASE_URL + "get_badge_list/";
            public static String DATA = "data";
        }

        public static class BoostmeInfo {
            public static String SERVICE_URL = BASE_URL + "boost_me_info/";
            public static String DATA = "data";
        }

    }
}
