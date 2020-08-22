package com.raddyr.scan.ui.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.scan.data.repository.ScanRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ShareSettingsViewModel @Inject constructor(repository: ScanRepository) : ViewModel() {

    var uuidRequest = MutableLiveData<Boolean>()

    var uuidResponse = Transformations.switchMap(uuidRequest) {
        repository.uuid()
    }

    var shareRequest = MutableLiveData<String>()
    var shareResponse = Transformations.switchMap(shareRequest) {
        repository.share(it)
    }
}