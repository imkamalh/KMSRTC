package com.dev.it.kmskedelainew.utils;

/**
 * Created by jemsnaban on 4/20/2016.
 */
public class URLEndpoints {
    public static String MAIN_URL = "http://www.kmskedelai.pe.hu/";
    public static String PUBLIC_URL = "http://www.kmskedelai.pe.hu/public/default/home/";

    public static String SEND_BUKU_TAMU = MAIN_URL + "bukutamu/insert_buku_tamu";
    public static String GET_PENYULUH = MAIN_URL + "datapenyuluh/read_all_penyuluh";
    public static String GET_ARTICLES_NEW = MAIN_URL + "artikel/get_artikel_terbaru";
    public static String GET_ARTICLES_POPULAR = MAIN_URL + "artikel/get_artikel_terpopuler";
    public static String UPDATE_HIT = MAIN_URL + "artikel/hit/";
    public static String GET_LIST_FERMENTASI = MAIN_URL + "pengolahanhasil/get_fermentasi";
    public static String GET_LIST_NON_FERMENTASI = MAIN_URL + "pengolahanhasil/get_non_fermentasi";
    public static String GET_LIST_KEDELAI = MAIN_URL + "kedelai/get_kedelai_info";
    public static String GET_LIST_TEKNOLOGI = MAIN_URL + "teknologi/api_get_teknologi/";
    public static String REPORT = MAIN_URL + "extras/submit_report/";
    public static String GET_FORUMS = MAIN_URL + "konsultasi/get_forums";
    public static String GET_TOPICS = MAIN_URL + "konsultasi/get_topics";
    public static String GET_COMEENTS_TOPICS = MAIN_URL + "konsultasi/get_komentar_topik/";
    public static String SUBMIT_COMEENTS_TOPICS = MAIN_URL + "konsultasi/submit_komentar";
    public static String DELETE_COMEENTS_TOPICS = MAIN_URL + "konsultasi/delete_komentar/";
    public static String GET_FILE_LIST = MAIN_URL + "filemanagement/get_file_list";

    public static String GET_COMEENTS_ARTICLES = MAIN_URL + "artikel/get_komen_artikel/";
    public static String SUBMIT_COMEENTS_ARTICLES = MAIN_URL + "artikel/submit_komen_artikel";
    public static String DELETE_COMEENTS_ARTICLES = MAIN_URL + "artikel/delete_komen_artikel/";

    //user management
    public static String REGISTER_USER = MAIN_URL + "usermanagement/register_user";
    public static String LOGIN_USER = MAIN_URL + "usermanagement/login_user";
    public static String EDIT_PROFILE = MAIN_URL + "usermanagement/edit_profile";
    public static String CHECK_PASSWORD = MAIN_URL + "usermanagement/checkpassword";
    public static String FORGOT_PASSWORD = MAIN_URL + "usermanagement/forgot_password";
    public static String NEW_PASSWORD = MAIN_URL + "usermanagement/newpassword";
    //public static String CHANGE_PICTURE = "http://kmskedelaiapp.web.id/upload.php";
    public static String CHANGE_PICTURE = MAIN_URL + "usermanagement/edit_profilepic";
    public static String CEK_AVATAR = MAIN_URL + "usermanagement/cek_avatar/";

    //webviews
    public static String GET_ARTICLES_WEBVIEW = MAIN_URL + "artikel/webview/";
    public static String GET_ABOUT_WEBVIEW = MAIN_URL + "tentang/webview/1";
    public static String GET_PENGOLAHAN_WEBVIEW = MAIN_URL + "pengolahanhasil/webview/";
    public static String GET_KEDELAI_WEBVIEW = MAIN_URL + "kedelai/webviewkedelai/";
    public static String GET_TEKNOLOGI_WEBVIEW = MAIN_URL + "teknologi/teknologiwebview/";
    public static String GET_EXTRAS_WEBVIEW = MAIN_URL + "extras/webview/";

    //folders
    public static String AVATARS_FOLDER = MAIN_URL + "assets/avatars/";
    public static String AVATARUSERS_FOLDER = MAIN_URL + "assets/avataruser/";
    public static String ARTICLE_FOLDER = MAIN_URL + "assets/images/articles/";
    public static String PENGOLAHAN_FOLDER = MAIN_URL + "assets/images/pengolahan/";
    public static String KEDELAI_FOLDER = MAIN_URL + "assets/images/kedelai/";
    public static String FILE_FOLDER = MAIN_URL + "assets/fileuploads/";

    public static String KEDELAI_PUBLIC = PUBLIC_URL + "kedelai/";
    public static String ARTICLE_PUBLIC = PUBLIC_URL + "artikel/";
    public static String TEKNOLOGI_PUBLIC = PUBLIC_URL + "teknologiproduksi/";
    public static String PENGOLAHAN_PUBLIC = PUBLIC_URL + "pengolahanhasil/";
    public static String TOPIK_PUBLIC = PUBLIC_URL + "topikdiskusi/";
}
