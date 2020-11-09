package com.example.riss.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.HomeScreen;
import com.example.riss.MainActivity;
import com.example.riss.R;
import com.example.riss.databinding.FragmentHomeBinding;
import com.example.riss.databinding.HomeViewBinding;
import com.example.riss.interfaces.IUserProfileInterface;
import com.example.riss.models.HomeModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.FUND_STATISTICS;
import static com.example.riss.AppUtils.Utils.TotalFundAmount;
import static com.example.riss.AppUtils.Utils.TotalFundManger;
import static com.example.riss.AppUtils.Utils.VALUE;
import static com.example.riss.AppUtils.Utils.checkUserProfile;
import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.example.riss.AppUtils.Utils.showSnackBar;
import static com.google.common.net.HttpHeaders.FROM;


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


        homeBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });
    }

    private void init() {
        showAlertDialog(requireActivity());
        checkUserProfile(new IUserProfileInterface() {
            @Override
            public void isProfileVerified(boolean b) {

            }

            @Override
            public void profileData(DocumentSnapshot snapshot) {
                homeBinding.setUser(snapshot);
            }

            @Override
            public void isProfileCompleted(boolean isProfileCom) {
                hideAlertDialog();
                if (isProfileCom) {
                    setRecData();
                } else navController.navigate(R.id.action_homeFragment_to_profileFragment);
            }

            @Override
            public void onError(String msg) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        getFirestoreReference().collection(FUND_STATISTICS)
                .document(TotalFundAmount)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            try {
                                homeBinding.tvTotalFundAmount.setText(getCountInRomanFormat(value.getLong(VALUE)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        getFirestoreReference().collection(FUND_STATISTICS)
                .document(TotalFundManger)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            try {
                                homeBinding.tvTotalFundManager.setText(getCountInRomanFormat(value.getLong(VALUE)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }


    private void setRecData() {
        list.clear();
        homeScreenAdapter = new HomeScreenAdapter(list);
        homeBinding.homeScreenRec.setAdapter(homeScreenAdapter);
        loadData();
    }

    private void loadData() {
        list.add(new HomeModel("Top", "Funds", R.drawable.savings));
        list.add(new HomeModel("My Personal", "Funds", R.drawable.my_personal_fund_image));
        list.add(new HomeModel("Support a", "Funds", R.drawable.support_fund_image));
        list.add(new HomeModel("Miracle", "Drops", R.drawable.pills));
        //list.add(new HomeModel("Service", "Distribute", R.drawable.image_distribution_center));
        list.add(new HomeModel("Distribution", "History", R.drawable.growth));
        list.add(new HomeModel("Create a", "Funds", R.drawable.create_fund_image));
        list.add(new HomeModel("Search a", "Funds", R.drawable.search_fund_image));
        list.add(new HomeModel("My Favourite", "Funds", R.drawable.my_personal_fund_image));
        //list.add(new HomeModel("Public", "Reviews", R.drawable.public_review_image));
        list.add(new HomeModel("Write a", "Review", R.drawable.survey));
        //list.add(new HomeModel("Donate for", "Miracle Drops", R.drawable.donate));

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
            HomeViewBinding homeViewBinding = HomeViewBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(homeViewBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeScreenAdapter.ViewHolder holder, final int position) {

            HomeModel model = homeModelList.get(position);
            holder.binding.setHomeView(model);

            holder.binding.cvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            navController.navigate(R.id.action_homeFragment_to_topFundsFragment);
                            break;
                        case 5:
                            Bundle bundle = new Bundle();
                            bundle.putString(FROM, "HomeFragment");
                            navController.navigate(R.id.action_homeFragment_to_createFundFragment, bundle);
                            break;
                        case 6:
                            //navController.navigate(R.id.action_homeFragment_to_createFundFragment);
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
            HomeViewBinding binding;

            public ViewHolder(HomeViewBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

}