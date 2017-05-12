// Фрагмент со списком уроков
// Отображает список уроков
// Вызывает WebActivity при нажатии на урок

package evoytenkoapps.mangohelper.ru;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class LessonsListFragment extends Fragment
{
    static RecyclerView rv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rv = (RecyclerView) inflater.inflate(R.layout.fragment_lessons_recycleview, container, false);
        setupRecyclerView(rv);
        return rv;
    }

//    // Загружаем список пройденных уроков
//    public void loadSharedPreferenses()
//    {
//        sPrefLessons = this.getActivity().getSharedPreferences("LessonsResults", Context.MODE_PRIVATE);
//    }


    private void setupRecyclerView(RecyclerView recyclerView)
    {
        //this.getResources().getStringArray(R.string.listoflessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), getLessonsList()));
    }

    // Задаем список уроков
    private List<String> getLessonsList()
    {
        int amount = Lessons.sLessonsStrings.length;
        ArrayList<String> list = new ArrayList<>(amount);
        while (list.size() < amount)
        {
            // Заполняем лист названиями уроков из Lessons
            list.add(Lessons.sLessonsStrings[list.size()][0]);
        }
        return list;
    }


    //Отработка перерисования окна, при возврате к ней
    @Override
    public void onResume()
    {
        super.onResume();
        //loadSharedPreferenses();
        //Toast.makeText(null, "Refreshed", Toast.LENGTH_SHORT).show();
        Log.d("Glide", "onResume");
        if (rv.getAdapter() != null)
        {
            rv.getAdapter().notifyDataSetChanged();
            Log.d("Glide", "Adapter  notifyDataSetChanged");
        }
    }


    // Адаптер
    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>
    {
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            // Номер урока, передается в webactivity
            public int mLessonNumber;
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(R.id.text1);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position)
        {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items)
        {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        // Создаем холдер с полезной нагрузкой
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        //Создаем список уроков
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            holder.mLessonNumber = position + 1;
            // Увеличиваем позицион на 1 что не было 0 урока
            holder.mTextView.setText((position + 1) + " " + mValues.get(position));
            // Если урок пройден
            String finished = MainActivity.sPrefLessons.getString((position + 1) + "", "");
            if (finished.equals("1"))
            {
                // Меняем его цвет
//                 
//                Glide.with(holder.mImageView.getContext()).load(R.drawable.ic_lessondone)
//                    //.load(Cheeses.getRandomCheeseDrawable())
//                    .fitCenter()
//                    .into(holder.mImageView);
//                Log.d("Glide", " Image Yes");
                holder.mTextView.setTextColor(R.color.black);
                holder.mImageView.setImageResource(R.drawable.ic_lessondone);
            } else
            {
                holder.mTextView.setTextColor(R.color.black);
                holder.mImageView.setImageResource(R.color.transparent);
                Log.d("Glide", "Image No");

            }

            // Вызов WebActivity
            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, WebActivity.class);
                    // Передаем вебвью номер урока и запускаем ее
                    intent.putExtra(WebActivity.EXTRA_NAME, holder.mLessonNumber);
                    context.startActivity(intent);
                    //((Activity) context).startActivityForResult(intent, 1);
                }
            });
        }

        // Определяем количество выводимых строк
        @Override
        public int getItemCount()
        {
            return mValues.size();
        }
    }
}
