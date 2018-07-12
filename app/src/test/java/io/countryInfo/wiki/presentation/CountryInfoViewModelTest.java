package io.countryInfo.wiki.presentation;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.countryInfo.wiki.model.Status;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class CountryInfoViewModelTest {

    @Rule
    // Must be added to every test class that targets app code that uses RxJava
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    @InjectMocks
    private CountryInfoViewModel viewModel;


    @Test
    public void testOnCountryInfoFetch() {
        assertEquals("About Canada", viewModel.getCountryInfoLiveData().getValue().getCountryInfo().getTitle());
        assertNull(viewModel.getCountryInfoLiveData().getValue().getMessage());
        assertEquals(Status.SUCCESS, viewModel.getCountryInfoLiveData().getValue().getStatus());
    }
}
