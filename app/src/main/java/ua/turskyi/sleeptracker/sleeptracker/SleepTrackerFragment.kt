package ua.turskyi.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ua.turskyi.sleeptracker.R
import ua.turskyi.sleeptracker.database.SleepDatabase
import ua.turskyi.sleeptracker.databinding.FragmentSleepTrackerBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        val sleepTrackerViewModel = ViewModelProviders.of(
            this,
            viewModelFactory
        ).get(SleepTrackerViewModel::class.java)

        binding.sleepTrackerViewModel = sleepTrackerViewModel

        binding.lifecycleOwner = this

        sleepTrackerViewModel.showSnackbarEvent.observe(
            this, Observer {
                if (it == true) {
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    sleepTrackerViewModel.doneShowingSnackbar()
                }
            }
        )

        sleepTrackerViewModel.navigateToSleepQuality.observe(this,
            Observer { night ->
                night?.let {
                    this.findNavController().navigate(
                        SleepTrackerFragmentDirections
                            .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                    sleepTrackerViewModel.doneNavigating()
                }
            })

        sleepTrackerViewModel.navigateToSleepDataQuality.observe(this,
            Observer {night ->
                night?.let {
                    this.findNavController().navigate(
                        SleepTrackerFragmentDirections
                            .actionSleepTrackerFragmentToSleepDetailFragment(night))
                    sleepTrackerViewModel.onSleepDataQualityNavigated()
                }
            })

        val manager = GridLayoutManager(activity, 3)
        //making header to place first three list items in order to look nice in the whole row
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
        binding.sleepList.layoutManager = manager

        val adapter = SleepNightAdapter(SleepNightListener {
//            nightId -> Toast.makeText(context, "$nightId", Toast.LENGTH_SHORT).show()
                nightId ->
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })
        binding.sleepList.adapter = adapter

        sleepTrackerViewModel.nights.observe(
            viewLifecycleOwner, Observer {
                it?.let {
//                    adapter.data = it
                    adapter.addHeaderAndSubmitList(it)
                }
            }
        )

        return binding.root
    }
}
