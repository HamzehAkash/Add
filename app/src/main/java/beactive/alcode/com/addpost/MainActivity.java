package beactive.alcode.com.addpost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
         {

    EditText mTitle, mSub, mDesc, mPrice;
    RadioGroup mRadioGroup;
    Spinner mCatogiry, mSubC;
    PostDetails mPostDetails;
    private static final int THUMB_SIZE = 200;

    Cloudinary cloudinary;

    int flag = 0;

    String mCato, mSubCa;

    public String getID() {
        return "glkdgdlkgbndlkbnfglkbnfglk";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Map config = new HashMap();
        config.put("cloud_name", "dgmp8uhzo");
        config.put("api_key", "141456213961645");
        config.put("api_secret", "V-8Lt9zWLIBfgp0cD4dHc-kIzEA");
        cloudinary = new Cloudinary(config);

        mPostDetails = new PostDetails();



        mPostDetails.setDate(new Date().toString());

        mTitle = (EditText) findViewById(R.id.txt_title);
        mSub = (EditText) findViewById(R.id.txt_sub);
        mDesc = (EditText) findViewById(R.id.txt_desc);
        mPrice = (EditText) findViewById(R.id.txt_price);


        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        mCatogiry = (Spinner) findViewById(R.id.spinner);
        mSubC = (Spinner) findViewById(R.id.spinner2);


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton) {
                    mPrice.setEnabled(false);
                } else {
                    mPrice.setEnabled(true);
                }
            }
        });

        String[] catogry = getResources().getStringArray(R.array.catogries);
        //  Toast.makeText(getApplicationContext(),catogry[0]+"",Toast.LENGTH_LONG).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, catogry);
        mCatogiry.setAdapter(adapter);

        mCatogiry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCato = (String) parent.getItemAtPosition(position);

                mPostDetails.setCategory(mCato);
                String[] arraySub = new String[0];
                if (mCato.equals("cars")) {
                    arraySub = getResources().getStringArray(R.array.cars);
                } else if (mCato.equals("electronic")) {
                    arraySub = getResources().getStringArray(R.array.electronic);
                }

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arraySub);
                mSubC.setAdapter(adapter1);

                mSubC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mSubCa = (String) parent.getItemAtPosition(position);
                        mPostDetails.setSubCategory(mSubCa);
                        //   Toast.makeText(getApplicationContext(),mSubCa,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void submit(View view) {
        if (!mTitle.getText().toString().equals(""))
            mPostDetails.setTitle(mTitle.getText() + "");
        else {
            Toast.makeText(getApplicationContext(), "Title is empty", Toast.LENGTH_LONG).show();
            return;
        }

        if(!mSub.getText().toString().isEmpty())
        {
            mPostDetails.setTitle(mTitle.getText() + "");
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Sub title is empty", Toast.LENGTH_LONG).show();
            return;
        }

        if(mPostDetails.getImageUrl().isEmpty()||mPostDetails.getPosterUrl().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must upload",Toast.LENGTH_LONG).show();
            return;
        }


        if(!mDesc.getText().toString().isEmpty())
        {
            mPostDetails.setLongDescription(mDesc.getText() + "");
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Descrption is empty", Toast.LENGTH_LONG).show();
            return;
        }

        if(mPrice.isEnabled())
        {
            if(!mPrice.getText().toString().isEmpty())
            {
                mPostDetails.setFees(mPrice.getText().toString());
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Descrption is empty", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else
        {
            mPostDetails.setFees("free");
        }

/*
        Firebase firebase=new Firebase(Constant.mFirebase+"item");

        firebase.push().setValue(mPostDetails);

*/





    }

    public void uploadPoster(View view) {
        flag = 1;
        loadGellary();
    }


    public void uploadImage(View view)

    {
        flag = 2;
        loadGellary();
    }

    private void loadGellary() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            Uri uri = data.getData();

            //Uri imgUri = "content://media/external/images/media/92";
            try {
                InputStream in = getContentResolver().openInputStream(uri);

                //String imageIdentifier = "image:upload:Raad.jpg";
                //  String[] components = imageIdentifier.split(":");

                //  String url = cloudinary.url().resourceType(components[0]).type(components[1]).generate(components[2]);
                //Log.d("hello",url);
                Random random = new Random();
                int ran;
                ran = random.nextInt(1000000) + 1;
                if (flag == 1) {
                    String keyPath = getID() + "getPostID" + ran;
                    cloudinary.uploader().upload(in, Cloudinary.asMap("public_id", keyPath));
                    String path = cloudinary.url().transformation(new Transformation().width(THUMB_SIZE).height(THUMB_SIZE).crop("fill")).generate(keyPath + ".jpg");

                    mPostDetails.setPosterUrl(path);

                    Log.d("hello", cloudinary.url().toString());
                    //   cloudinary.url().generate("sample_remote.jpg");

                    Toast.makeText(getApplicationContext(), "Sucess Upload", Toast.LENGTH_LONG).show();
                    //object code

                } else {
                    String keyPath = getID() + "getImageID" + ran;
                    cloudinary.uploader().upload(in, Cloudinary.asMap("public_id", keyPath));
                    String path = cloudinary.url().transformation(new Transformation().width(THUMB_SIZE).height(THUMB_SIZE).crop("fill")).generate(keyPath + ".jpg");


                    mPostDetails.setImageUrl(path);
                    Log.d("hello", cloudinary.url().toString());
                    //   cloudinary.url().generate("sample_remote.jpg");

                    Toast.makeText(getApplicationContext(), "Sucess Upload", Toast.LENGTH_LONG).show();
                    //object code
                }
                //   cloudinary.url().generate("sample_remote.jpg");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed Upload", Toast.LENGTH_LONG).show();
                Log.d("Hello", e.toString());

            }
            //  http://res.cloudinary.com/demo/image/upload/sample_remote.jpg


/*
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Log.d(TAG, String.valueOf(bitmap));

                 //  Bitmap bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.image);//your image
*/
        }
    }




}
