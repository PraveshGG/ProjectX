package com.example.android.projectx.HomeScreen.ProfileFragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.projectx.EditDescription.EditDescriptionActivity;
import com.example.android.projectx.HomeScreen.HomeActivity;
import com.example.android.projectx.EditDescription.ModelUser;
import com.example.android.projectx.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    ImageView editDetailsIcon;
    TextView headerName, fullName, age, dob, gender, status, education, birthPlace, currentLocation, bloodType, additionalContacts, work, headerCurrentLocation, headerBirthplace;
    View view;
    CircleImageView headerImageView;
    SharedPreferences preferences;
    private static int RESULT_LOAD_IMG = 1;
    Bitmap headerImageBm = null;
    Bundle imagePathBndl;
    boolean noImage;
    Profile mcall;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        imagePathBndl = new Bundle();


        headerName = view.findViewById(R.id.name);
        headerImageView = (CircleImageView) view.findViewById(R.id.profile);
        fullName = view.findViewById(R.id.fullname);
        age = view.findViewById(R.id.age);
        dob = view.findViewById(R.id.dob);
        gender = view.findViewById(R.id.gender);
        status = view.findViewById(R.id.status);
        education = view.findViewById(R.id.education);
        work = view.findViewById(R.id.work);
        birthPlace = view.findViewById(R.id.birthPlace);
        currentLocation = view.findViewById(R.id.current_location);
        bloodType = view.findViewById(R.id.blood_type);
        additionalContacts = view.findViewById(R.id.additonalContacts);
        editDetailsIcon = view.findViewById(R.id.edit);
        headerBirthplace = view.findViewById(R.id.location);
        headerCurrentLocation = view.findViewById(R.id.designation);

        headerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra("crop", "true");
                photoPickerIntent.putExtra("scale", true);
                photoPickerIntent.putExtra("outputX", 256);
                photoPickerIntent.putExtra("outputY", 256);
                photoPickerIntent.putExtra("aspectX", 1);
                photoPickerIntent.putExtra("aspectY", 1);
                photoPickerIntent.putExtra("return-data", true);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

                //mcall.sendimage("/storage/emulated/0/profilePic.png");
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setMessage("sasdf");
//                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent j = new Intent(getContext(),HomeActivity.class);
//                        startActivityForResult(j, requestCode);
//                    }
//                });
//                dialog.show();
            }
        });


        if (!getImage("/profilePic.png").exists()) {

            headerImageView.setImageResource(R.drawable.man);

        } else {

                File file = getImage("/profilePic.png");
                String path = file.getAbsolutePath();

                imagePathBndl.putString("imgpth", path);
                Intent intent = getActivity().getIntent();
                intent.putExtras(imagePathBndl);
                mcall.sendimage(path);
                if (path != null) {
                    headerImageView.setImageBitmap(BitmapFactory.decodeFile(path));
                }

            }



        if (getArguments().getString("model") == null || getArguments().getString("model") == "") {

        } else {
            String model = getArguments().getString("model");

            ModelUser user = new Gson().fromJson(model, ModelUser.class);
            if (user.getfName() == null || user.getfName().equals("")) {
                fullName.setText("N/A");
            } else {
                fullName.setText(user.getfName() + " " + user.getmName() + " " + user.getlName());
            }

            if (user.getfName() == null || user.getfName().equals("")) {
                headerName.setText("User");
            } else {
                if (user.getmName()==null||user.getmName().equals("")) {
                    headerName.setText(user.getfName() + " " + user.getlName());
                }
                else{
                    headerName.setText(user.getfName() + " " + user.getmName() + " " + user.getlName());
                }

            }


            if (user.getAge() == null || user.getAge().equals("")) {
                age.setText("N/A");
            } else {
                age.setText(user.getAge());
            }

            if (user.getDateOfBirth() == null || user.getAge().equals("")) {
                dob.setText("N/A");
            } else {
                dob.setText(user.getDateOfBirth());
            }


            if (user.getGender() == null) {
                gender.setText("N/A");
            } else {
                gender.setText(user.getGender());
            }

            if (user.getStatus() == null) {
                status.setText("N/A");
            } else {
                status.setText(user.getStatus());
            }

            if (user.getEducation() == null) {
                education.setText("N/A");
            } else {
                education.setText(user.getEducation());
            }

            if (user.getBirthPlace().equals(null) || user.getBirthPlace().equals("")) {
                birthPlace.setText("N/A");
            } else {
                birthPlace.setText(user.getBirthPlace());
            }
            Log.d("fis", "onCreateView: " + user.getBirthPlace().length());

            if (user.getBirthPlace().equals(null) || user.getBirthPlace().equals("")) {
                headerBirthplace.setText("Birthplace");
            } else {
                headerBirthplace.setText(user.getBirthPlace());
            }
            Log.d("fis", "onCreateView: " + user.getBirthPlace().length());


            if (user.getDesignation() == null || user.getDesignation().equals("")) {
                currentLocation.setText("N/A");
            } else {
                currentLocation.setText(user.getDesignation());
            }

            if (user.getDesignation() == null || user.getDesignation().equals("")) {
                headerCurrentLocation.setText("(Current Location)");
            } else {
                headerCurrentLocation.setText("(" + user.getDesignation() + ")");
            }


            if (user.getBloodGroup() == null) {
                bloodType.setText("N/A");
            } else {
                bloodType.setText(user.getBloodGroup());
            }


            if (user.getWork() == null) {
                work.setText("N/A");
            } else {
                work.setText(user.getWork());
            }


            if (user.getAdditionalContacts() == null || user.getAdditionalContacts().equals("")) {
                additionalContacts.setText("N/A");
            } else {
                additionalContacts.setText(user.getAdditionalContacts());
            }
        }

        editDetailsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getContext(), EditDescriptionActivity.class);
                startActivity(newIntent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap ProfilePic = extras.getParcelable("data");
                headerImageBm = ProfilePic;
                headerImageView.setImageBitmap(ProfilePic);
                storeImage(headerImageBm, "profilePic");

                HomeActivity homeActivity = (HomeActivity) getContext();
                homeActivity.setImage();
            }
        }


    }

    public static boolean storeImage(Bitmap bitmap, String filename) {

        boolean stored = false;

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, filename + ".png");

        if (file.exists())
            file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            stored = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mcall = (Profile) activity;
        } catch (Exception ex) {
        }
    }

    public interface Profile {
        void sendimage(String imagepath);
    }
}
