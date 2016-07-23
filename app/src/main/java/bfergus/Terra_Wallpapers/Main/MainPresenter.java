package bfergus.Terra_Wallpapers.Main;




public interface MainPresenter {

    void saveImageToGallery();

    String getFileNameForTodaysImage();

    void onResume();

    void onDestroy();



}
