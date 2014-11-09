package com.greenwang.ExTextView;

import java.util.ArrayList;

import com.greenwang.ExpandableTextView.ExpandableTextView;
import com.greenwang.ExpandableTextView.ExpandableTextView.CollapseListener;
import com.greenwang.ExpandableTextViewSample.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

    private ListView mListView;

    private ListAdapter mAdapter = new ListAdapter();

    private ArrayList<ListItem> mList = new ArrayList<MainActivity.ListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList.add(new ListItem("Apache License"));
        mList.add(new ListItem("Version 2.0, January 2004"));
        mList.add(new ListItem("http://www.apache.org/licenses/"));
        mList.add(new ListItem("TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION"));
        mList.add(new ListItem("1. Definitions."));
        mList.add(new ListItem(
                "\"License\" shall mean the terms and conditions for use, reproduction, "
                        + "and distribution as defined by Sections 1 through 9 of this document."));
        mList.add(new ListItem(
                "\"Licensor\" shall mean the copyright owner or entity authorized by"
                        + " the copyright owner that is granting the License."));
        mList.add(new ListItem("\"Legal Entity\" shall mean the union of the acting entity and all"
                + " other entities that control, are controlled by, or are under common"
                + " control with that entity. For the purposes of this definition,"
                + " \"control\" means (i) the power, direct or indirect, to cause the"
                + " direction or management of such entity, whether by contract or"
                + " otherwise, or (ii) ownership of fifty percent (50%) or more of the"
                + " outstanding shares, or (iii) beneficial ownership of such entity."));
        mList.add(new ListItem("\"You\" (or \"Your\") shall mean an individual or Legal Entity"
                + " exercising permissions granted by this License."));
        mList.add(new ListItem(
                "\"Source\" form shall mean the preferred form for making "
                        + " modifications, including but not limited to software source code, documentation"
                        + " source, and configuration files."));
        mList.add(new ListItem("\"Object\" form shall mean any form resulting from mechanical"
                + " transformation or translation of a Source form, including but"
                + " not limited to compiled object code, generated documentation,"
                + " and conversions to other media types."));
        mList.add(new ListItem("\"Work\" shall mean the work of authorship, whether in Source or"
                + " Object form, made available under the License, as indicated by a"
                + " copyright notice that is included in or attached to the work"
                + " (an example is provided in the Appendix below)."));
        mList.add(new ListItem("\"Work\" shall mean the work of authorship, whether in Source or"
                + " Object form, made available under the License, as indicated by a"
                + " copyright notice that is included in or attached to the work"
                + " (an example is provided in the Appendix below)."));
        mList.add(new ListItem("\"Contribution\" shall mean any work of authorship, including"
                + " the original version of the Work and any modifications or additions"
                + " to that Work or Derivative Works thereof, that is intentionally"
                + " submitted to Licensor for inclusion in the Work by the copyright owner"
                + " or by an individual or Legal Entity authorized to submit on behalf of"
                + " the copyright owner. For the purposes of this definition, \"submitted\""
                + " means any form of electronic, verbal, or written communication sent"
                + " to the Licensor or its representatives, including but not limited to"
                + " communication on electronic mailing lists, source code control systems,"
                + " and issue tracking systems that are managed by, or on behalf of, the"
                + " Licensor for the purpose of discussing and improving the Work, but"
                + " excluding communication that is conspicuously marked or otherwise"
                + " designated in writing by the copyright owner as \"Not a Contribution.\""));
        mList.add(new ListItem(
                "\"Contributor\" shall mean Licensor and any individual or Legal Entity"
                        + " on behalf of whom a Contribution has been received by Licensor and"
                        + " subsequently incorporated within the Work."));
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler hodler = null;
            if (convertView == null) {
                convertView =
                        LayoutInflater.from(getBaseContext()).inflate(
                                R.layout.list_item_ex_textview, null);
                hodler = new ViewHodler();
                hodler.exTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_tv);
                hodler.exTextView.setCollapseListener(hodler);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }

            hodler.item = mList.get(position);

            hodler.exTextView.setText(mList.get(position).info, hodler.item.collapsed);
            Log.e("==========", "" + hodler.exTextView.getText());
            return convertView;
        }

        class ViewHodler implements CollapseListener {
            ListItem item;
            ExpandableTextView exTextView;

            @Override
            public void onExpandClicked(View view) {
                Log.e("========", "Collapsed: " + item.collapsed);
                item.collapsed = !exTextView.isCollapsed();
                exTextView.setCollapse(item.collapsed);
                // exTextView.setText(exTextView.getText() + "1", item.collapsed);
                Log.e("========", "Collapsed: " + item.collapsed);
            }
        }

    }

    class ListItem {
        String info;
        boolean collapsed;

        public ListItem(String i) {
            collapsed = true;
            info = i;
        }
    }
}
