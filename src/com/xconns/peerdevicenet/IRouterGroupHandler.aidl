/*
 * Copyright (C) 2013 Yigong Liu, XCONNS, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xconns.peerdevicenet;

import android.os.Bundle;
import com.xconns.peerdevicenet.DeviceInfo;

interface IRouterGroupHandler {
	oneway void onError(in String errInfo);
	oneway void onSelfJoin(in DeviceInfo[] devices);
	oneway void onPeerJoin(in DeviceInfo device);
	oneway void onSelfLeave();
	oneway void onPeerLeave(in DeviceInfo device);
	oneway void onReceive(in DeviceInfo src, in byte[] msg);
	oneway void onGetPeerDevices(in DeviceInfo[] devices);
}
