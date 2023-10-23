package com.m3lnyk.memefriends;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class HelpTest {

    @Mock
    Context context;

    @Test
    public void testOnSourceCodeClicked(){
        Help help = new Help();

        Uri expectedUri = Uri.parse("https://github.com/M3LNYK/MemeFriends");
        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, expectedUri);

        help.onSourceCodeClicked();

        // Verify that startActivity is called with the expected intent.
        Mockito.verify(context).startActivity(Mockito.argThat(intent ->
                intent.getAction().equals(expectedIntent.getAction()) &&
                        intent.getData().toString().equals(expectedUri.toString())
        ));
    }
}