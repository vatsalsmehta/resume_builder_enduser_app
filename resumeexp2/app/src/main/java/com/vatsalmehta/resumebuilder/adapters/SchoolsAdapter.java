package com.vatsalmehta.resumebuilder.adapters;

import android.support.annotation.NonNull;

import com.vatsalmehta.resumebuilder.datamodel.School;

import java.util.List;



public class SchoolsAdapter extends ResumeEventAdapter<School> {

    public SchoolsAdapter(@NonNull List<School> list,
                          ResumeEventOnClickListener resumeEventOnClickListener) {
        super(list, resumeEventOnClickListener);
    }
}