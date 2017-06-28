package offline.mapbox.trackerapp.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import offline.mapbox.trackerapp.constant.Constant;

public class FileUtils {
    public static String SDCARD_PAHT = Environment
            .getExternalStorageDirectory().getPath();

    public static String DCIMCamera_PATH = Environment
            .getExternalStorageDirectory() + "/DCIM/Camera/";

    /**
     * Check if sdcard is available
     *
     * @return true is available; false is not available
     */
    public static boolean isSDAvailable()
    {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED))
            return false;
        return true;
    }

    public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth, int newHeight)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    public static Bitmap ResizeBitmap(Bitmap bitmap, int scale)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(1/scale, 1/scale);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    public static String getNewFileName()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());

        return formatter.format(curDate);
    }

    /**
     *
     * @param context
     * Context
     *
     * @param bm
     * Bitmap to be saved
     *
     * @param name
     * Save the name can be null, according to the time to customize a file name
     *
     * @return to ".jpg" format to the album
     *
     */
    public static Boolean saveBitmapToCamera(Context context, Bitmap bm,
                                             String name)
    {

        File file = null;

        if (name == null || name.equals(""))		{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            name = formatter.format(curDate) + ".jpg";
        }

        file = new File(DCIMCamera_PATH, name);
        if (file.exists())		{
            file.delete();
        }

        try		{
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e)		{
            e.printStackTrace();
            return false;
        } catch (IOException e)		{
            e.printStackTrace();
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        return true;
    }

    public static String saveBitmapToCameraWithDate(Context context, Bitmap bm, boolean isProfile,
                                                    String name)
    {

        File file = null;

        if (name == null || name.equals(""))
        {

//			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyyHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            name = "marker"+formatter.format(curDate) + ".jpg";
        }

        String subFolder = "";
        if(isProfile){
            subFolder = Constant.IMAGE_UPLOADING;
        } else {
            subFolder = Constant.IMAGE_CARD_FOLDER;
        }

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File fileDir = new File(filepath, Constant.TRACKER_FOLDER + "/" + subFolder);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

//		file = new File(DCIMCamera_PATH, name);
        file = new File(filepath + "/" + Constant.TRACKER_FOLDER + "/" + subFolder, name);
        if (file.exists())
        {
            file.delete();
        }

        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        return file.getPath();
    }

    /**
     *
     * @param bitmap
     * @param destPath
     * @param quality
     */
    public static void writeImage(Bitmap bitmap, String destPath, int quality)
    {
        try
        {
            deleteFile(destPath);
            if (createFile(destPath))
            {
                FileOutputStream out = new FileOutputStream(destPath);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
                {
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean createFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                if (!file.getParentFile().exists())
                {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static File createNewFile(String filePath) {

        File file = new File(filePath);
        if (!file.exists())
        {
            if (!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }

            return file;
        }
        return file;
    }

    /**
     * delete
     *
     * @param filePath
     *
     * @return true if this file was deleted, false otherwise
     */
    public static boolean deleteFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            if (file.exists())
            {
                return file.delete();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete all the files in the dir directory, including deleting the deleted folders
     *
     * @param dir
     */
    public static void deleteDirectory(File dir)
    {
        if (dir.isDirectory())
        {
            File[] listFiles = dir.listFiles();
            for (int i = 0; i < listFiles.length; i++)
            {
                deleteDirectory(listFiles[i]);
            }
        }
        dir.delete();
    }


    /** Get File Path from a Uri*/
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
