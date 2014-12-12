package com.mstr.letschat.tasks;

import java.lang.ref.WeakReference;

import android.content.Context;

import com.mstr.letschat.SmackInvocationException;
import com.mstr.letschat.model.UserProfile;
import com.mstr.letschat.tasks.Response.Listener;
import com.mstr.letschat.xmpp.XMPPContactHelper;

public class SendContactRequestTask extends BaseAsyncTask<Void, Void, Boolean> {
	private WeakReference<Context> contextWrapper;
	private WeakReference<UserProfile> userProfileWrapper;
	
	public SendContactRequestTask(Listener<Boolean> listener, Context context, UserProfile userProfile) {
		super(listener);
		
		contextWrapper = new WeakReference<Context>(context);
		userProfileWrapper = new WeakReference<UserProfile>(userProfile);
	}
	
	@Override
	protected Response<Boolean> doInBackground(Void... params) {
		Context context = contextWrapper.get();
		UserProfile userProfile = userProfileWrapper.get();
		if (context != null && userProfile != null) {
			try {
				
				XMPPContactHelper.getInstance().addContact(userProfile.getJid(), userProfile.getNickname());
				
				return Response.success(true);
			} catch (SmackInvocationException e) {
				return Response.error(e);
			}
		} else {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Response<Boolean> response) {
		Listener<Boolean> listener = getListener();
		
		if (listener != null && response != null) {
			if (response.isSuccess()) {
				listener.onResponse(response.getResult());
			} else {
				listener.onErrorResponse(response.getException());
			}
		}
	}
}