package gpc.edo.asqrcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.zxing.WriterException;

import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

/**
 * Created by Edo on 1/24/2018.
 */

public class generatepage extends AppCompatActivity {

    BootstrapEditText textarea;
    ImageView hasilgenerate;
    BootstrapButton btn_generate;
    String TAG = "GenerateQRCode";
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generatepage);

        textarea = (BootstrapEditText) findViewById(R.id.textArea_information);
        hasilgenerate = (ImageView) findViewById(R.id.defaultqrcode);
        btn_generate = (BootstrapButton) findViewById(R.id.btn_generate);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textarea.getText().toString().equals("")) {
                    Toast.makeText(generatepage.this, "Tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {

                    inputValue = textarea.getText().toString().trim();
                    if (inputValue.length() > 0) {
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3 / 4;

                        qrgEncoder = new QRGEncoder(
                                inputValue, null,
                                QRGContents.Type.TEXT,
                                smallerDimension);
                        try {
                            bitmap = qrgEncoder.encodeAsBitmap();
                            hasilgenerate.setImageBitmap(bitmap);
                            hasilgenerate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(generatepage.this);
                                    alertDialogBuilder.setTitle("Pesan").setMessage("Mau menyimpan gambar ?").setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            boolean save;
                                            String result;
                                            int angkarandom = new Random().nextInt();
                                            try {
                                                save = QRGSaver.save(savePath, "QRCodeGenerate"+angkarandom, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                                                result = save ? "Gambar Berhasil disimpan" : "Gambar tidak berhasil disimpan karena ada error.";
                                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(generatepage.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                                }
                            });
                        } catch (WriterException e) {
                            Log.v(TAG, e.toString());
                        }
                    } else {
                        textarea.setError("Required");
                    }
                }

            }

        });



    }
}
