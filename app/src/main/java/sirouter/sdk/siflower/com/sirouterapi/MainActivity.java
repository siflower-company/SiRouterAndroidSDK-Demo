package sirouter.sdk.siflower.com.sirouterapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import java.util.List;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import sirouter.sdk.siflower.com.locallibrary.siwifiApi.LocalApi;
import sirouter.sdk.siflower.com.locallibrary.siwifiApi.Model.WiFiInfo;
import sirouter.sdk.siflower.com.locallibrary.siwifiApi.SFException;
import sirouter.sdk.siflower.com.locallibrary.siwifiApi.ret.BindRet;
import sirouter.sdk.siflower.com.remotelibrary.SFServer.Listener.RemoteConnectionListener;
import sirouter.sdk.siflower.com.remotelibrary.SFServer.Listener.SFObjectResponseListener;
import sirouter.sdk.siflower.com.remotelibrary.SFServer.Listener.SiWiFiListListener;
import sirouter.sdk.siflower.com.remotelibrary.SFServer.SFClass.Routers;
import sirouter.sdk.siflower.com.remotelibrary.SFServer.SFClass.SFUser;
import sirouter.sdk.siflower.com.remotelibrary.SiWiFiManager;

public class MainActivity extends AppCompatActivity {

    private SFUser sfUser;

    private Button bind;
    private Button get_wifi;

    private Routers routers;

    private static final String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind = findViewById(R.id.bind);
        get_wifi = findViewById(R.id.get_wifi);

        SiWiFiManager.init(this, "c20ad4d76fe97759aa27a0c99bff6710");

        sfUser = SFUser.getCacheUser(this);
        if (sfUser != null && !sfUser.getLoginkey().equals("")) {
            SFUser.loginByKey(this, sfUser.getLoginkey(), new SFObjectResponseListener<SFUser>() {

                @Override
                public void onSuccess(SFUser sfUser) {
                    Log.e(TAG, "login success" + new Gson().toJson(sfUser));
                    MainActivity.this.sfUser = sfUser;
                    if (sfUser.getBinder() != null) {
                        if (sfUser.getBinder().size() != 0) {
                            Log.e(TAG, "not zero");
                            routers = sfUser.getBinder().get(0);
                        }

                    }
                    SiWiFiManager.getInstance().createRemoteConnection(sfUser, new RemoteConnectionListener() {
                        @Override
                        public void onConnectSuccess() {
                            Log.e(TAG, "on connection success");
                        }

                        @Override
                        public void onConnectionClose(int code, String reason) {
                            Log.e(TAG, "on connection close");
                        }

                        @Override
                        public void onFailure(Exception ex) {
                            Log.e(TAG, "on Failure");
                        }
                    });
                }

                @Override
                public void onError(SFException ex) {
                    Log.e(TAG, "login error" + ex.getMessage());
                }
            });
        } else {
            SFUser.loginByExtra(this, "999556", new SFObjectResponseListener<SFUser>() {
                @Override
                public void onSuccess(SFUser sfUser) {
                    Log.e(TAG, "login success" + new Gson().toJson(sfUser));
                    MainActivity.this.sfUser = sfUser;
                    if (sfUser.getBinder() != null) {
                        if (sfUser.getBinder().size() != 0) {
                            Log.e(TAG, "not zero");
                            routers = sfUser.getBinder().get(0);
                        }

                    }
                    SiWiFiManager.getInstance().createRemoteConnection(sfUser, new RemoteConnectionListener() {
                        @Override
                        public void onConnectSuccess() {
                            Log.e(TAG, "on connection success");
                        }

                        @Override
                        public void onConnectionClose(int code, String reason) {
                            Log.e(TAG, "on connection close");
                        }

                        @Override
                        public void onFailure(Exception ex) {
                            Log.e(TAG, "on Failure");
                        }
                    });
                }

                @Override
                public void onError(SFException ex) {
                    Log.e(TAG, "login error" + ex.getMessage());
                }
            });
        }


        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sfUser == null) {
                    return;
                }

                SiWiFiManager.getInstance().bindSiRouter(MainActivity.this, LocalApi.DEFAULT_APP_API_VERSION, sfUser, new SingleObserver<BindRet>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BindRet bindRet) {
                        Log.e(TAG, "bind success ");
                        SiWiFiManager.getInstance().getRouters(sfUser, new SiWiFiListListener<Routers>() {
                            @Override
                            public void onSuccess(List<Routers> objlist) {
                                for (Routers routers : objlist) {
                                    if (routers.getObjectId().equals(bindRet.getRouterobjectid())) {
                                        MainActivity.this.routers = routers;
                                    }
                                }
                            }

                            @Override
                            public void onError(int code, String msg) {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "bind error " + e.getMessage());
                    }
                });
            }
        });

        get_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sfUser == null) {
                    return;
                }

                if (routers == null) {
                    return;
                }

                SiWiFiManager.getInstance().getWifiObserve(routers, sfUser, new SingleObserver<List<WiFiInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<WiFiInfo> wiFiInfos) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        });
    }
}
