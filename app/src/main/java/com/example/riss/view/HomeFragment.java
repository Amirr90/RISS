package com.example.riss.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.HomeScreen;
import com.example.riss.MainActivity;
import com.example.riss.Models.HomeModel;
import com.example.riss.R;
import com.example.riss.databinding.FragmentHomeBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.showSnackBar;


public class HomeFragment extends Fragment {

    FragmentHomeBinding homeBinding;
    HomeScreenAdapter homeScreenAdapter;
    List<HomeModel> list = new ArrayList<>();
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return homeBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        navController = Navigation.findNavController(view);
        homeBinding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void init() {
        setRecData();
    }


    private void setRecData() {
        list.clear();
        homeScreenAdapter = new HomeScreenAdapter(list);
        homeBinding.homeScreenRec.setAdapter(homeScreenAdapter);
        loadData();
    }

    private void loadData() {
        list.add(new HomeModel(1 + ". Top Funds"));
        list.add(new HomeModel(2 + ". Miracle Drops"));
        list.add(new HomeModel(3 + ". Distribute"));
        list.add(new HomeModel(4 + ". Public review"));
        list.add(new HomeModel(5 + ". Donates For M Drops"));
        list.add(new HomeModel(6 + ". Search A funds"));
        list.add(new HomeModel(7 + ". Supports A funds"));
        list.add(new HomeModel(8 + ". Create A funds"));
        list.add(new HomeModel(9 + ". My funds"));
        list.add(new HomeModel(10 + ". My favourites funds"));

        /*for (int a = 0; a < 20; a++) {
            list.add(new HomeModel(a + " title"));
        }*/
        homeScreenAdapter.notifyDataSetChanged();
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(HomeScreen.getInstance())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomeScreen.getInstance(), "Logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeScreen.getInstance(), MainActivity.class));
                        HomeScreen.getInstance().finish();
                    }
                });
    }

    private class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.ViewHolder> {
        List<HomeModel> homeModelList;

        public HomeScreenAdapter(List<HomeModel> homeModelList) {
            this.homeModelList = homeModelList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.home_view, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeScreenAdapter.ViewHolder holder, final int position) {

            HomeModel model = homeModelList.get(position);
            holder.textView.setText(model.getTitle());

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            navController.navigate(R.id.action_homeFragment_to_topFundsFragment);
                            break;
                        case 5:
                            navController.navigate(R.id.action_homeFragment_to_searchFundFragment);
                            break;
                        default:
                            showSnackBar("Coming Soon", v);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return homeModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View view) {
                super(view);
                textView = view.findViewById(R.id.textView9);
            }
        }
    }
}