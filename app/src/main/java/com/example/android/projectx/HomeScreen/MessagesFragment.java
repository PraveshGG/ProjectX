package com.example.android.projectx.HomeScreen;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.projectx.ModelUser;
import com.example.android.projectx.R;
import com.example.android.projectx.Reminder.ReminderAdapter;
import com.example.android.projectx.Reminder.ReminderTagModel;
import com.example.android.projectx.Reminder.SetReminderActivity;
import com.example.android.projectx.Reminder.ViewReminderActivity;
import com.example.android.projectx.Splash.SplashActivity;
import com.example.android.projectx.ViewSpecificReminderActivity;
import com.example.android.projectx.WelcomeRegister.SaveReminderModel;
import com.example.android.projectx.WelcomeRegister.WelcomeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.android.projectx.HomeScreen.PeopleFragments.PeopleFragment.drawableToBitmap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    View view;
    ListView reminderListView;
    ViewGroup viewGroup;
    TextView numOfReminders;
    ReminderAdapter reminderAdapter;
    SharedPreferences sharedPreferences,reminderSizeSP;
    ArrayList<String> dateLocationList;
    Boolean isdeleted = true;
    Bitmap bitmap;
    ArrayList<ReminderTagModel> taggedReminderList;
    ArrayList<SaveReminderModel> deletedReminderList;
    ArrayList<SaveReminderModel> reminderList;
    FloatingActionButton fab;
    TextDrawable textDrawable;
    CircleImageView reminderImage;


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_messages, container, false);
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("LOading");
        dialog.setCancelable(true);
        dialog.show();




        dateLocationList = new ArrayList<>();
        deletedReminderList = new ArrayList<>();

        sharedPreferences = getContext().getSharedPreferences("RSP", MODE_PRIVATE);
        reminderSizeSP = getContext().getSharedPreferences("RSSP", MODE_PRIVATE);

        String reminderListString = sharedPreferences.getString("savedFormData", null);



        Type type = new TypeToken<ArrayList<SaveReminderModel>>() {
        }.getType();
        reminderList = new Gson().fromJson(reminderListString, type);

        fab = view.findViewById(R.id.fab_add_frag);
        reminderListView = view.findViewById(R.id.reminder_view_list_frag);
        viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_reminder_header, reminderListView, false);
        numOfReminders = viewGroup.findViewById(R.id.totalReminders);
        reminderListView.addHeaderView(viewGroup, null, false);
        reminderImage = view.findViewById(R.id.reminder_random_image);




        deletedReminderList = new Gson().fromJson(sharedPreferences.getString("deletedList", null)
                , new TypeToken<ArrayList<SaveReminderModel>>() {
                }.getType());

        if (deletedReminderList==null) {
            Toast.makeText(getContext(), "is null", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("drL", "onCreate: " + deletedReminderList.size());
            Toast.makeText(getContext(), "deleted item at postion 0: (sub)" + deletedReminderList.size(), Toast.LENGTH_SHORT).show();

        }

        if (reminderList == null) {

        } else {
            for (int i = 0; i < reminderList.size(); i++) {
                dateLocationList.add(reminderList.get(i).getTvReminder1());
            }
            taggedReminderList = new ArrayList<>();



            numOfReminders.setText("Total Reminders: " + reminderList.size());
            reminderAdapter = new ReminderAdapter(getActivity(), reminderList, dateLocationList);
            reminderListView.setAdapter(reminderAdapter);

            if (getContext() instanceof HomeActivity){
                ((HomeActivity)getContext()).sendSize(reminderList.size());
            }

            SharedPreferences.Editor editor = reminderSizeSP.edit();
            editor.putInt("size",reminderList.size());
            editor.commit();


            dialog.dismiss();


            reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, final View view, int position, long id) {

                    Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    position = position - 1;
                    final int finalPosition = position;


                    CharSequence actions[] = new CharSequence[]{"Edit", "View", "Delete", "Share"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Reminder Actions...");
                    builder.setItems(actions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]
                            switch (which) {
                                case 0:
                                    String reminderListString = sharedPreferences.getString("savedFormData", null);
                                    Type type = new TypeToken<ArrayList<SaveReminderModel>>() {
                                    }.getType();
                                    reminderList = new Gson().fromJson(reminderListString, type);
//
                                    startActivity(new Intent(getActivity(), SetReminderActivity.class)
                                            .putExtra("pos",finalPosition)
                                            .putExtra("editornew","edit")
                                            .putExtra("editReminder",new Gson().toJson(reminderList.get(finalPosition))));
                                    break;
                                case 1:
                                    getActivity().finish();
                                    startActivity(getActivity().getIntent());

                                    String viewReminderList = sharedPreferences.getString("savedFormData", null);

                                    type = new TypeToken<ArrayList<SaveReminderModel>>() {
                                    }.getType();
                                    reminderList = new Gson().fromJson(viewReminderList, type);

                                    startActivity(new Intent(getActivity(), ViewSpecificReminderActivity.class)
                                            .putExtra("view", new Gson().toJson(reminderList.get(finalPosition))));
                                    break;
                                case 2:
                                    showDialogOK("Delete Entry?",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which) {
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            taggedReminderList = new ArrayList<>();
                                                            Toast.makeText(getActivity(), "item pos: " + finalPosition, Toast.LENGTH_SHORT).show();
                                                            taggedReminderList.add(new ReminderTagModel(finalPosition, reminderList.get(finalPosition)));
                                                            reminderList.remove(finalPosition);


                                                            dateLocationList.clear();
                                                            for (int i = 0; i < reminderList.size(); i++) {
                                                                dateLocationList.add(reminderList.get(i).getTvReminder1());
                                                                Log.d("yeh", "onCreate: " + reminderList.get(i).getTvReminder1());

                                                            }
                                                            reminderAdapter = new ReminderAdapter(getContext(), reminderList, dateLocationList);
                                                            reminderListView.setAdapter(reminderAdapter);
                                                            numOfReminders.setText("Your Reminders: " + reminderList.size());
                                                            if (getContext() instanceof HomeActivity){
                                                                ((HomeActivity)getContext()).sendSize(reminderList.size());
                                                            }

                                                            if (reminderList.size() == 1) {
                                                                numOfReminders.setText("Total Reminder: " + reminderList.size());

                                                            }
                                                            Snackbar snackbar = Snackbar
                                                                    .make(parent, "Reminder Deleted.", Snackbar.LENGTH_LONG)
                                                                    .setAction("Undo", new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            int post = taggedReminderList.get(0).getPos();
                                                                            Log.d("zxlc", "onClick: " + post);
                                                                            reminderList.add(post, taggedReminderList.get(0).getList());

                                                                            dateLocationList.clear();
                                                                            for (int i = 0; i < reminderList.size(); i++) {
                                                                                dateLocationList.add(reminderList.get(i).getTvReminder1());
                                                                                Log.d("zxc", "size: " + dateLocationList.size());

                                                                            }
                                                                            reminderAdapter = new ReminderAdapter(getContext(), reminderList, dateLocationList);
                                                                            reminderListView.setAdapter(reminderAdapter);
                                                                            numOfReminders.setText("Total Reminders: " + reminderList.size());
                                                                            if (getContext() instanceof HomeActivity){
                                                                                ((HomeActivity)getContext()).sendSize(reminderList.size());
                                                                            }
                                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                            editor.putString("savedFormData", new Gson().toJson(reminderList));
                                                                            editor.commit();
                                                                            isdeleted = false;
                                                                        }
                                                                    });
                                                            snackbar.setActionTextColor(Color.WHITE);
                                                            snackbar.show();
                                                            snackbar.addCallback(new Snackbar.Callback() {

                                                                @Override
                                                                public void onDismissed(Snackbar snackbar, int event) {
                                                                    //see Snackbar.Callback docs for event details
                                                                    if (!isdeleted) {
                                                                        taggedReminderList.clear();
                                                                    } else {
                                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                        Toast.makeText(getContext(), "" + reminderList.size(), Toast.LENGTH_SHORT).show();
                                                                        editor.putString("savedFormData", new Gson().toJson(reminderList));
                                                                        editor.commit();

                                                                        deletedReminderList = new ArrayList<>();
                                                                        deletedReminderList.add(taggedReminderList.get(0).getList());
                                                                        editor.putString("deletedList", new Gson().toJson(deletedReminderList));
                                                                        editor.commit();
                                                                        taggedReminderList.clear();
                                                                    }
//                        Toast.makeText(ViewReminderActivity.this, "la dismissed vayo hai ta", Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onShown(Snackbar snackbar) {

                                                                }
                                                            });


                                                            break;
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            // proceed with logic by disabling the related features or quit the app.
                                                            break;
                                                    }
                                                }
                                            });
                                    break;
                                case 3:
                                    SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("sp", MODE_PRIVATE);
                                    SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("SP", MODE_PRIVATE);
                                    String firstName = "";
                                    String model = sharedPreferences2.getString("models", "");


                                    if (model == null || model == "") {

                                    } else {
                                        ModelUser nameUser = new Gson().fromJson(model, ModelUser.class);
                                        if (nameUser.getfName() == null || nameUser.getfName() == "") {
                                            firstName = "User";
                                        } else
                                            firstName = nameUser.getfName().toString();
                                    }
                                    String phoneNumber = sharedPreferences1.getString("phoneNumber", "");
                                    String name = "";
                                    for (int i = 0; i < reminderList.get(finalPosition).getTo().size(); i++) {
                                        name = name + reminderList.get(finalPosition).getTo().get(i).getName() + ", ";
                                    }
                                    name = name.replaceAll(", $", "");

                                    String shareBody = "From: " + firstName.trim() + " (" + phoneNumber + ")." +
                                            "\n\nTo: " + name + "." +
                                            "\n\nReminder type: " + reminderList.get(finalPosition).getTvReminder() +
                                            " (" + reminderList.get(finalPosition).getTvReminder1().replaceAll(":", "") + ")." +
                                            "\n\nSubject: " + reminderList.get(finalPosition).getSubject() +
                                            "\n\nMessage: \"" + reminderList.get(finalPosition).getCompose_message() + "\"";

                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");

                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(sharingIntent, "Share Using"));

                                    break;

                            }


                        }
                    });
                    builder.show();

                    return true;
                }
            });
        }

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().finish();
                startActivity(getActivity().getIntent());
                position=position-1;
                String viewReminderList = sharedPreferences.getString("savedFormData", null);

                Type type = new TypeToken<ArrayList<SaveReminderModel>>() {
                }.getType();
                reminderList = new Gson().fromJson(viewReminderList, type);

                startActivity(new Intent(getContext(), ViewSpecificReminderActivity.class)
                        .putExtra("view", new Gson().toJson(reminderList.get(position))));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getContext(),HomeActivity.class);
//                String a = "yes";
//                i.putExtra(a,"yes");
//                startActivity(i);
                if(getContext() instanceof HomeActivity){
                    ((HomeActivity) getContext()).onMethodCallback();}
            }
        });


        return view;
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", okListener).create();
        dialog.setNegativeButton("No", okListener).create();
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(COLOR_YOU_WANT);
//        dialog.create();
        dialog.setCancelable(false);
        dialog.show();


    }

}
