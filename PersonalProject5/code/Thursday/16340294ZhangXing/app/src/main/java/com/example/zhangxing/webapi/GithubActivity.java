package com.example.zhangxing.webapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public class GithubActivity extends AppCompatActivity {
    public interface GithubService{
        @GET("/users/{user}/repos")
        Observable<List<Repo>> getRepo(@Path("user") String user_name);
    }
    private RecyclerView recyclerView;
    private List<Repo> data;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private EditText editText;
    private Button button;
    private String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        data = new ArrayList<Repo>();
        editText = (EditText)findViewById(R.id.github_edit);
        button = (Button)findViewById(R.id.github_search);

        recyclerView = (RecyclerView)findViewById(R.id.github_recycler);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter<Repo>(GithubActivity.this, R.layout.list_github,data) {
            @Override
            public void convert(MyViewHolder holder, Repo o) {
                TextView repoName = holder.getView(R.id.repo_name);
                TextView repoId = holder.getView(R.id.repo_id);
                TextView repoProblem = holder.getView(R.id.repo_problem);
                TextView repoDiscription = holder.getView(R.id.repo_discription);
                repoName.setText("项目名："+o.getName());
                repoId.setText("项目ID："+o.getId());
                if(o.getDescription()==null){
                    repoDiscription.setText("项目描述：null");
                }else{
                    repoDiscription.setText("项目描述："+o.getDescription());
                }

                repoProblem.setText("存在问题："+o.getOpen_issues());
            }
        };
        recyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(GithubActivity.this,IssueActivity.class);
                Repo repo = data.get(position);
                repo.setDescription(username);
                intent.putExtra("repo", repo);
                startActivity(intent);
            }
            @Override
            public void onLongClick(final int position) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                username = editText.getText().toString();
                OkHttpClient build = new OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.SECONDS)
                        .readTimeout(2, TimeUnit.SECONDS)
                        .writeTimeout(2, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(build)
                        .build();

                GithubService githubService = retrofit.create(GithubService.class);
                Observable<List<Repo>> call = githubService.getRepo(username);
                call.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Repo>>(){
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(List<Repo> o) {
                                if(o.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"仓库为空",Toast.LENGTH_SHORT).show();
                                }else{
                                    for(int i = 0;i < o.size();i++){
                                        if(o.get(i).has_issues==true){
                                            data.add(o.get(i));
                                        }
                                    }
                                    myRecyclerViewAdapter.notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                            }
                        });

            }
        });
    }

}
