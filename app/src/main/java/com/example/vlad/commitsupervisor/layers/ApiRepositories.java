package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;

import com.example.vlad.commitsupervisor.Commit;
import com.example.vlad.commitsupervisor.User;

import java.util.List;

/**
 * Created by taraskreknin on 18/10/2017.
 */

interface ApiRepositories extends Api {

    @NonNull
    List<String> getUserRepositories(@NonNull final User user);

    @NonNull
    List<Commit> getCommits(@NonNull final String repoName, @NonNull final User user);

}
