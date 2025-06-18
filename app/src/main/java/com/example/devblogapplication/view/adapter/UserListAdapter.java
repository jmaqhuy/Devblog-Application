package com.example.devblogapplication.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.databinding.UserItemBinding;
import com.example.devblogapplication.model.UserDTO;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private final List<UserDTO> users;
    private final OnUserClickListener listener;

    public UserListAdapter(List<UserDTO> users, OnUserClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDTO user = users.get(position);
        holder.binding.setUser(user);
        holder.binding.setListener(listener);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private UserItemBinding binding;

        public UserViewHolder(@NonNull UserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnUserClickListener {
        void onUserClick(UserDTO user);
        void onFollowClick(UserDTO user);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<UserDTO> newUsers) {
        this.users.clear();
        if (newUsers != null) {
            this.users.addAll(newUsers);
        }
        notifyDataSetChanged();
    }
}
