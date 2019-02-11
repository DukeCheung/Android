package com.example.zhangxing.webapi;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Script;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class IssueActivity extends AppCompatActivity {
    public interface IssueService{
        @GET("/repos/{user}/{repo}/issues")
        Observable<List<Issue>> getIssue(@Path("user") String user_name, @Path("repo") String repo_name);

        @Headers("Authorization: token 70540956e058e6c590a1dc9dc3507484c8ac9338")
        @POST("/repos/{user}/{repo}/issues")
        Observable<Issue> postIssue(@Path("user") String user_name, @Path("repo") String repo_name, @Body PostIssues issues);
    }

    private RecyclerView recyclerView;
    private List<Issue> data;
    private Repo repo;
    private MyRecyclerViewAdapter<Issue> myRecyclerViewAdapter;
    private EditText issueToken, issueTitle, issueBody;
    private Button issueButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        data = new ArrayList<Issue>();
        recyclerView = (RecyclerView)findViewById(R.id.issue_recycler);
        issueToken = (EditText)findViewById(R.id.issueToken);
        issueTitle = (EditText)findViewById(R.id.issueTitle);
        issueBody = (EditText)findViewById(R.id.issueBody);
        issueButton = (Button)findViewById(R.id.issueButton);
        Intent intent = this.getIntent();
        repo = (Repo) intent.getSerializableExtra("repo");
        myRecyclerViewAdapter = new MyRecyclerViewAdapter<Issue>(IssueActivity.this,R.layout.list_issue,data) {
            @Override
            public void convert(MyViewHolder holder, Issue issue) {
                TextView title = holder.getView(R.id.issue_title);
                TextView time = holder.getView(R.id.issue_time);
                TextView state = holder.getView(R.id.issue_state);
                TextView body = holder.getView(R.id.issue_body);
                title.setText("Title: "+issue.getTitle());
                time.setText("创建时间: "+issue.getCreated_at());
                state.setText("问题状态: "+issue.getState());
                body.setText("问题描述: "+issue.getBody());
            }
        };
        recyclerView.setAdapter(myRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(repo.getOpen_issues()==0){
            Toast.makeText(getApplicationContext(),"这个项目没有issues",Toast.LENGTH_SHORT).show();
        }else{
            data.clear();
            String username = repo.getDescription();
            String reponame = repo.getName();
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

            IssueService issueService = retrofit.create(IssueService.class);
            Observable<List<Issue>> call = issueService.getIssue(username,reponame);
            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Issue>>(){
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(List<Issue> o) {
                            data.addAll(o);
                            myRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }

        issueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = repo.getDescription();
                String reponame = repo.getName();

                PostIssues postIssues = new PostIssues();
                postIssues.setTitle(issueTitle.getText().toString());
                postIssues.setBody(issueBody.getText().toString());

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

                IssueService issueService = retrofit.create(IssueService.class);
                Observable<Issue> call = issueService.postIssue(username,reponame,postIssues);
                call.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Issue>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Issue issue) {
                                data.add(issue);
                                myRecyclerViewAdapter.notifyDataSetChanged();
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
