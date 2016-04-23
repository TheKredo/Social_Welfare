package learner.sukesh.socialwelfare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Sukesh on 10-04-2016.
 */
public class IssueList extends ArrayAdapter<String>{


        private final Activity context;
        String[] title;
        String[] name;
        TextView titleTextView,nameTextView;


        public IssueList(Activity context,String[] title,String[] name) {
            super(context,R.layout.issue_type,title);
            this.context=context;
            this.title=title;
            this.name=name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            final View rowView=inflater.inflate(R.layout.issue_type,null,true);

            titleTextView= (TextView) rowView.findViewById(R.id.text_view_list_name);
            nameTextView=(TextView)rowView.findViewById(R.id.text_view_created_by_user);

            titleTextView.setText(title[position]);
            nameTextView.setText(name[position]);

            return rowView;
        }
    }
