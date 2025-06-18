package com.example.devblogapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.databinding.BottomSheetMenuForReportReasonBinding;
import com.example.devblogapplication.databinding.BottomSheetMenuForSettingBinding;
import com.example.devblogapplication.databinding.TagItemBinding;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.model.UserInfoDTO;
import com.example.devblogapplication.view.adapter.RankTagAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BottomMenu {
    public static void showPostBottomMenu(Context context, PostDTO post) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu_for_post, null);

        view.findViewById(R.id.report).setOnClickListener(v -> {
            showReportBottomMenu(context, post);
        });
        view.findViewById(R.id.cancel).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();
    }

    public static void showReportBottomMenu(Context context, PostDTO postDTO) {
        BottomSheetDialog reportDialog = new BottomSheetDialog(context);
        View reportView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu_for_report_reason, null);
        BottomSheetMenuForReportReasonBinding binding = BottomSheetMenuForReportReasonBinding.bind(reportView);
        // Use a mutable set for selected tags
        Set<Tag> selectedTags = new HashSet<>();
        binding.flexbox.removeAllViews();
        if (postDTO.getTags() != null) {
            for (Tag tag : postDTO.getTags()) {
                TextView tagView = createTagTextView(context, binding.flexbox, tag, (view, id) -> {
                    view.setSelected(false);
                    if (selectedTags.contains(tag)) {
                        selectedTags.remove(tag);
                        view.setSelected(false);
                    } else {
                        selectedTags.add(tag);
                        view.setSelected(true);
                    }
                });
                binding.flexbox.addView(tagView);
            }
        }
        binding.rbUnrelated.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.rbSpam.setChecked(false);
                binding.rbWrongTag.setChecked(false);
                binding.rbAnother.setChecked(false);
                binding.flexbox.setVisibility(View.GONE);
            }
        });
        binding.rbSpam.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.rbUnrelated.setChecked(false);
                binding.rbWrongTag.setChecked(false);
                binding.rbAnother.setChecked(false);
                binding.flexbox.setVisibility(View.GONE);
            }
        });

        binding.rbWrongTag.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.rbUnrelated.setChecked(false);
                binding.rbSpam.setChecked(false);
                binding.flexbox.setVisibility(View.VISIBLE);
                binding.rbAnother.setChecked(false);
            }
        });

        binding.rbAnother.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.rbUnrelated.setChecked(false);
                binding.rbSpam.setChecked(false);
                binding.rbWrongTag.setChecked(false);
                binding.flexbox.setVisibility(View.GONE);
            }
        });


        binding.reportBtn.setOnClickListener(btn -> {
            binding.errorTextView.setText("");
            binding.errorTextView.setVisibility(View.GONE);
            String reason = null;
            if (binding.rbUnrelated.isChecked()) {
                reason = binding.tvUnrelated.getText().toString();
            } else if (binding.rbSpam.isChecked()) {
                reason = binding.tvSpam.getText().toString();
            }
            if (reason != null) {
                // TODO: Handle the selected reason (e.g., send to server)
                reportDialog.dismiss();
            } else {
                binding.errorTextView.setText("Please select a reason for reporting.");
                binding.errorTextView.setVisibility(View.VISIBLE);
            }
        });
        binding.cancelBtn.setOnClickListener(btn -> reportDialog.dismiss());

        binding.etAdditionalInfo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (!binding.rbSpam.isChecked() && !binding.rbUnrelated.isChecked()
                        && !binding.rbWrongTag.isChecked() && !binding.rbAnother.isChecked()) {
                    binding.rbAnother.setChecked(true);
                }
            }
        });

        reportDialog.setContentView(reportView);
        reportDialog.setCancelable(true);
        reportDialog.setCanceledOnTouchOutside(true);
        reportDialog.show();
    }

    private static TextView createTagTextView(Context context, FlexboxLayout flexbox, Tag tag, RankTagAdapter.OnTagActionListener onClickListener) {
        TagItemBinding tagItemBinding = TagItemBinding.inflate(
                LayoutInflater.from(context),
                flexbox,
                false
        );

        tagItemBinding.setTag(tag);
        tagItemBinding.setListener(onClickListener);
        tagItemBinding.executePendingBindings();

        tagItemBinding.tagName.setTextSize(13);
        return tagItemBinding.tagName;
    }

    public static void ShowSettingBottomMenu(Context context, UserInfoDTO userInfo) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu_for_setting, null);
        BottomSheetMenuForSettingBinding binding = BottomSheetMenuForSettingBinding.bind(view);
        binding.editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.devblogapplication.view.activity.SetupProfileActivity.class);
            intent.putExtra("email", userInfo.getEmail());
            intent.putExtra("fullname", userInfo.getFullname());
            intent.putExtra("username", userInfo.getUsername());
            intent.putExtra("avatarUrl", userInfo.getAvatarLink());
            intent.putExtra("fromEditProfile", true);
            context.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        binding.editFavoriteTagsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.devblogapplication.view.activity.SelectFavoriteTagActivity.class);
            Gson gson = new Gson();
            String tagsJson = gson.toJson(userInfo.getFavoriteTags());
            intent.putExtra("SELECTED_TAGS_JSON", tagsJson);
            context.startActivity(intent);
            bottomSheetDialog.dismiss();
        });


        binding.logoutBtn.setOnClickListener(v -> {
            SecurePrefsHelper.clearAccessToken(context);
            UserRepository userRepository = new UserRepository(context);
            userRepository.deleteAllData();
            Intent intent = new Intent(context, com.example.devblogapplication.view.activity.LoginActivity.class);
            context.startActivity(intent);
            if (context instanceof Activity) ((Activity) context).finishAffinity();
            bottomSheetDialog.dismiss();
            // TODO: Handle logout action
        });
        binding.cancelBtn.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();
    }


}
