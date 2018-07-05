# MVVM_POC
Showing Facts about Country by fetching from cloud.
## Architectural design ##
->Using MVVM design pattern for separation of code.
->Using Architectural Components-
		-Room database for persistence.
		-ViewModel(AndroidViewModel) for surviving orientation change and providing
			data to Activity.
		-LiveData for automatically updating UI whenever data changes from database.
		-Activity(view) only knows about rendering data and maintaining its state.
->Using EventBus for sending various events across layers.
->Espresso UI unit testing.

Inspired from google's android architecture repo-
https://github.com/googlesamples/android-architecture-components


