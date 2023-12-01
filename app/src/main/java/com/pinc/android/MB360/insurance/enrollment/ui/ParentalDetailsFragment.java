package com.pinc.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.AddDependantBottomSheetBinding;
import com.pinc.android.MB360.databinding.FragmentParentalDetailsBinding;
import com.pinc.android.MB360.insurance.enrollment.adapters.ParentalDetailAdapter;
import com.pinc.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.pinc.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.Relation;
import com.pinc.android.MB360.insurance.enrollment.ui.bottomSheets.AddDependantBottomSheet;
import com.pinc.android.MB360.utilities.SwipeToDeleteCallBackParental;

import java.util.ArrayList;
import java.util.List;

public class ParentalDetailsFragment extends Fragment implements DependantHelper, DependantListener, SwipeToDeleteCallBackParental.RecyclerItemTouchHelperListener, ConfirmationDialogs.DialogActions {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    EnrollmentViewModel enrollmentViewModel;

    FragmentParentalDetailsBinding binding;
    View view;

    //parental data
    List<ParentalModel> parentalList = new ArrayList<>();

    ParentalDetailAdapter adapter;

    public ParentalDetailsFragment() {
        // Required empty public constructor
    }

    public ParentalDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
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
        binding = FragmentParentalDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //setting the view-model according to the scope
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);

        //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();

        //get the parental data
        getParentalData();

        //swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallBackParental(requireContext(), 0, ItemTouchHelper.LEFT, this::onSwiped, parentalList);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFamilyDetails);
        return view;
    }

    private void getParentalData() {
        enrollmentViewModel.getParental();

        enrollmentViewModel.getParentalData().observe(getViewLifecycleOwner(), parentalData -> {
            if (parentalData != null) {
                for (Relation relation : parentalData.getRelations()) {
                    parentalList.add(new ParentalModel(relation.getRelationName(), relation.getMaxCount(),
                            relation.getGroup(), false, "", "",
                            false, null, "",
                            true, true, "", false));
                }

                parentalList.add(0, new ParentalModel("", 1,
                        "", false, "", "",
                        false, null, "",
                        true, true, "", true));

                if (parentalList.size() > 3) {
                    //that means we get the cross parents data too
                    parentalList.add(3, new ParentalModel("", 1,
                            "", false, "", "",
                            false, null, "",
                            true, true, "", true));

                }


                adapter = new ParentalDetailAdapter(parentalList, requireContext(), this);
                binding.rvFamilyDetails.setAdapter(adapter);


            }
        });
    }


    @Override
    public void onAddDependant(DependantHelperModel dependantHelperModel, int position) {
        AddDependantBottomSheet parentBottomSheet = new AddDependantBottomSheet(this, dependantHelperModel, position);
        parentBottomSheet.setCancelable(true);
        parentBottomSheet.show(getChildFragmentManager(), parentBottomSheet.getTag());
    }

    @Override
    public void onEditDependant(DependantHelperModel dependant, int position) {
        AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position, true);
        dialog.show(getChildFragmentManager(), dialog.getTag());
        dialog.setCancelable(true);
    }

    @Override
    public void onDeleteDependant() {

    }

    @Override
    public void onEditTwin(int position) {

    }

    @Override
    public void onDependantSavedListener(DependantHelperModel dependant, int position) {
        //here we get the parent and we need to save the parent in the parent list

        for (DependantHelperModel dependantCycle : parentalList
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
                adapter.notifyItemChanged(position);
                adapter.notifyItemChanged(position);
            }

        }
    }

    @Override
    public void onTwinsAdded(DependantHelperModel twin1, DependantHelperModel twin2, int position, boolean edit) {
        //we dont need any twins here because we are currently adding parent
    }

    @Override
    public void onDependantEditedListener(DependantHelperModel dependant) {
        //here we listen to the changes to the editec parent which also can be listen by on addependant
    }

    @Override
    public void onDependantDeletedListener(DependantHelperModel dependant) {
        //here we should call the confirmation dialog with the item touch helper
        //swipe gesture confirmation is being called here
        //and then after confirmation we call delete method which
        //only updates the parent again to empty state!
    }

    // Deleting the dependant
    @Override
    public void onConfirmed(int position) {
        // this is on-confirmed of deleting
        ParentalModel dependant = parentalList.get(position);
        parentalList.set(position, new ParentalModel(dependant.getRelationName(), dependant.getMaxCount(), dependant.getGroup(),
                false, "", "", false, null, "", false, false, "", false));

        adapter.notifyItemChanged(position);
    }

    //canceled the delete process
    @Override
    public void onRejected(int position) {
        //on canceled deleting
        parentalList.get(position).setCanEdit(true);
        parentalList.get(position).setCanDelete(true);
        adapter.notifyItemChanged(position);

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        //start the deleting process
        ConfirmationDialogs confirmationDialogs = new ConfirmationDialogs(requireContext(), this, position);
        confirmationDialogs.setCancelable(false);
        confirmationDialogs.show(getChildFragmentManager(), confirmationDialogs.getTag());

    }

    @Override
    public void onResume() {
        super.onResume();
      /*  //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/
    }
}