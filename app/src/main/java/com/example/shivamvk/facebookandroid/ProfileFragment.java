package com.example.shivamvk.facebookandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private Button btLogoutButton;
    private ImageView ivProfileFragmentUserImage;
    private TextView tvProfileFragmentUserName;
    private TextView tvProfileFragmentUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfileFragmentUserImage = view.findViewById(R.id.iv_profile_fragment_user_image);
        tvProfileFragmentUserName = view.findViewById(R.id.tv_profile_fragment_user_name);
        tvProfileFragmentUserEmail = view.findViewById(R.id.tv_profile_fragment_user_email);
        btLogoutButton = view.findViewById(R.id.bt_logout_button);

        String imgurlstring = SharedPrefManager.getInstance(getContext()).getUserImage();
        imgurlstring = imgurlstring.replace("/profilepicture/","%2Fprofilepicture%2F");

        Picasso.get()
                .load(imgurlstring)
                .placeholder(R.drawable.placeholderboy)
                .into(ivProfileFragmentUserImage);

        tvProfileFragmentUserName.setText(SharedPrefManager.getInstance(getContext()).getUserName());
        tvProfileFragmentUserEmail.setText(SharedPrefManager.getInstance(getContext()).getUserEmail());

        btLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getContext()).resgiterCurrentUser(null, "NULL", null);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                getActivity().finish();
            }
        });
    }
}
