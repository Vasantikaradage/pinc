package com.pinc.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.AddTwinsBottomSheetBinding;
import com.pinc.android.MB360.databinding.FragmentDependantDetailsBinding;
import com.pinc.android.MB360.insurance.enrollment.adapters.DependantDetailsAdapter;
import com.pinc.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.pinc.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.Relation;
import com.pinc.android.MB360.insurance.enrollment.ui.bottomSheets.AddDependantBottomSheet;
import com.pinc.android.MB360.insurance.enrollment.ui.bottomSheets.AddTwinsBottomSheet;
import com.pinc.android.MB360.utilities.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.List;

public class DependantDetailsFragment extends Fragment implements DependantHelper, DependantListener, SwipeToDeleteCallback.RecyclerItemTouchHelperListener, ConfirmationDialogs.DialogActions {

    //TODO : SOLVE THE ISSUES FOR DELETING THE TWIN.
    //TODO : SOLVE THE ISSUES FOR THE EDITING THE PEOPLE.......................✅
    //TODO : REFINE THE
    //TODO : UNIT TEST ()

    FragmentDependantDetailsBinding binding;
    View view;
    //view-model for enrollment
    EnrollmentViewModel enrollmentViewModel;
    //recyclerview adapter
    DependantDetailsAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;

    //special data list for adding deleting and  editing of the dependant.
    List<DependantHelperModel> dependantList = new ArrayList<>();

    DependantHelperModel lgbtqModel = new DependantHelperModel("PARTNER", 1, "1",
            false, "",
            "", false, null, "", true, true, "");


    boolean LGBTQ = true; //done (need special handling of the bottom sheet)
    boolean daughter_not_married = false; //will be done we get the max age.
    boolean parents_allowed = false; //done
    boolean cross_combination = false;//need info
    boolean twins_allowed = true; //twins needed

    public DependantDetailsFragment() {
        // Required empty public constructor
    }

    public DependantDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDependantDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();

        //sharing the view model scope same as the activity (insurance activity)
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);

        getDependantDetails();

        enrollmentViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });


        binding.swipetoRefresh.setOnRefreshListener(this::getDependantDetails);

        binding.instCard1.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            viewPagerNavigationMenuHelper.onHideMenu();
            if (scrollY < 40) {
                viewPagerNavigationMenuHelper.onShowMenu();
            } else {
                viewPagerNavigationMenuHelper.onHideMenu();
            }
        });


        //swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallback(requireContext(), 0, ItemTouchHelper.LEFT, this, dependantList);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.dependantCycle);
        return view;
    }

    private void showLoading() {
        binding.swipetoRefresh.setRefreshing(true);
    }

    private void hideLoading() {
        binding.swipetoRefresh.setRefreshing(false);
    }

    private void getDependantDetails() {
        enrollmentViewModel.getDependantDetails();

        enrollmentViewModel.getDependantDetailsData().observe(getViewLifecycleOwner(), dependantDetailsResponse -> {
            if (dependantDetailsResponse != null) {
                dependantList.clear();
                for (Relation relation : dependantDetailsResponse.getRelations()
                ) {
                    dependantList.add(new DependantHelperModel(relation.getRelationName(), relation.getMaxCount(), relation.getGroup()
                            , false, "", "", false, null, "", true, true, "MALE"));
                }


                //for parents included in the policy or has seperated policy
                //daughter age
                //is differently-able allowed
                //is LGBTQ allowed.

                /*if (!parents_allowed) {
                    dependantList.removeIf(dependantParent -> {
                        return dependantParent.getRelationName().equalsIgnoreCase("mother") ||
                                dependantParent.getRelationName().equalsIgnoreCase("father") ||
                                dependantParent.getRelationName().equalsIgnoreCase("mother-in-law") ||
                                dependantParent.getRelationName().equalsIgnoreCase("mother in law") ||
                                dependantParent.getRelationName().equalsIgnoreCase("father-in-law") ||
                                dependantParent.getRelationName().equalsIgnoreCase("father in law");
                    });
                }*/

                if (daughter_not_married) {
                    //max age of daughter should be overridden here.
                }

                if (LGBTQ) {
                    dependantList.add(1, lgbtqModel);
                }

                if (twins_allowed) {
                    int position = 3;
                    if (LGBTQ) {
                        position = 4;
                    } else {
                        position = 3;
                    }
                    dependantList.add(position, new DependantHelperModel("TWINS", 1, "2",
                            false, "",
                            "", false, null, "", true, false, "MALE"));
                }
                adapter = new DependantDetailsAdapter(dependantList, requireContext(), this);
                binding.dependantCycle.setAdapter(adapter);
            }
        });
    }

    //on dependant clicked from recyclerview.
    @Override
    public void onAddDependant(DependantHelperModel dependant, int position) {
        //here we need to add the interface to get the details of the dependant
        //so that we can get the details of the dependant added and update the recyclerview.

        switch (dependant.getRelationName().toLowerCase()) {
            case "twins":
                AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position);
                twinsDialog.setCancelable(true);
                twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());

                break;
            default:
                AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position);
                dialog.show(getChildFragmentManager(), dialog.getTag());
                dialog.setCancelable(true);
        }


    }

    @Override
    public void onEditDependant(DependantHelperModel dependant, int position) {

        //here we edit the dependant the details
        //tip :- here we follow same procedure as adding the data
        //as the data in dependant details is never added but retained the state from empty
        //to filled dependant state.

        switch (dependant.getRelationName().toLowerCase()) {
            //we don't need this case
            case "twins":
               /* AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position);
                twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());
                twinsDialog.setCancelable(true);*/

                break;
            default:
                AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position, true);
                dialog.show(getChildFragmentManager(), dialog.getTag());
                dialog.setCancelable(true);
        }


    }

    @Override
    public void onDeleteDependant() {

    }

    @Override
    public void onEditTwin(int position) {
        AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position, true);
        twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());
        twinsDialog.setCancelable(true);

    }

    //on dependant changed in bottom sheets
    @Override
    public void onDependantSavedListener(DependantHelperModel dependant, int position) {
        for (DependantHelperModel dependantCycle : dependantList
        ) {
            if (dependantCycle.getRelationName().equalsIgnoreCase(dependant.getRelationName())) {
                dependantCycle.setDateOfBirth(dependant.getDateOfBirth());
                dependantCycle.setName(dependant.getName());
                dependantCycle.setIsAdded(true);
                dependantCycle.setIsDifferentlyAble(dependant.isDifferentlyAble());
                dependantCycle.setDocument(dependant.getDocument());
                dependantCycle.setCanEdit(true);
                dependantCycle.setCanDelete(true);
                dependantCycle.setAge(dependant.getAge());
            }

            adapter.notifyItemChanged(position);
        }

        if (LGBTQ) {
            if (dependant.getRelationName().equalsIgnoreCase("spouse")) {
                dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("partner")));
            } else if (dependant.getRelationName().equalsIgnoreCase("partner")) {
                dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("spouse")));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTwinsAdded(DependantHelperModel twin1, DependantHelperModel twin2, int position, boolean edit) {
        //here we add twins with 2 dependant classes.
        if (edit) {
            enrollmentViewModel.setTwin1(twin1);
            enrollmentViewModel.setTwin2(twin2);
            dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("twins")));
            int twins_position = 3;
            if (dependantList.contains(lgbtqModel)) {
                if (dependantList.get(1).getIsAdded()) {
                    twins_position = 3;
                }
                twins_position = 4;
            } else {
                twins_position = 3;
            }

            dependantList.add(twins_position, twin1);
            dependantList.add(twins_position + 1, twin2);

            adapter.notifyDataSetChanged();

        } else {

            for (DependantHelperModel dependantCycle : dependantList
            ) {
                if (dependantCycle.getRelationName().equalsIgnoreCase(twin1.getRelationName())) {
                    dependantCycle.setDateOfBirth(twin1.getDateOfBirth());
                    dependantCycle.setName(twin1.getName());
                    dependantCycle.setIsAdded(true);
                    dependantCycle.setAge(twin1.getAge());
                    dependantCycle.setIsDifferentlyAble(twin1.isDifferentlyAble());
                    dependantCycle.setDocument(twin1.getDocument());
                    dependantCycle.setCanDelete(true);
                    enrollmentViewModel.setTwin1(dependantCycle);
                }
                adapter.notifyItemChanged(position);
            }

            enrollmentViewModel.setTwin2(twin2);
            dependantList.add(position + 1, twin2);
            adapter.notifyItemInserted(position + 1);
        }


    }

    @Override
    public void onDependantEditedListener(DependantHelperModel dependant) {

    }

    @Override
    public void onDependantDeletedListener(DependantHelperModel dependant) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        //here we can listen that it is swiped
        //logic of resetting the data
        //ask for confirmation
        ConfirmationDialogs confirmationDialogs = new ConfirmationDialogs(requireContext(), this, position);
        confirmationDialogs.setCancelable(false);
        confirmationDialogs.show(getChildFragmentManager(), confirmationDialogs.getTag());

    }

    @Override
    public void onConfirmed(int position) {
        //here we get the confirmation for deleting a dependant
        //and we delete
        int twins_position = 3;
        if (dependantList.contains(lgbtqModel)) {
            if (dependantList.get(1).getIsAdded()) {
                twins_position = 3;
            }
            twins_position = 4;
        } else {
            twins_position = 3;
        }
        if (dependantList.get(position).getRelationName().equalsIgnoreCase("twins")) {
            dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("twins")));
            dependantList.add(twins_position, new DependantHelperModel("TWINS", 2, "1",
                    false, "", "", false, null, "", false, false, ""));

        } else {
            DependantHelperModel dependant = dependantList.get(position);
            dependantList.set(position, new DependantHelperModel(dependant.getRelationName(), dependant.getMaxCount(), dependant.getGroup(),
                    false, "", "", false, null, "", false, false, ""));
            adapter.notifyItemChanged(position);
        }
        if (dependantList.get(position).getRelationName().equalsIgnoreCase("spouse")) {

            dependantList.add(1, new DependantHelperModel("PARTNER", 1, "1",
                    false, "", "", false, null, "", false, false, ""));
        } else if (dependantList.get(position).getRelationName().equalsIgnoreCase("partner")) {
            dependantList.add(0, new DependantHelperModel("SPOUSE", 1, "1",
                    false, "", "", false, null, "", false, false, ""));

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRejected(int position) {
        //user does not want to delete the dependant.
        dependantList.get(position).setCanEdit(true);
        dependantList.get(position).setCanDelete(true);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/
    }
}