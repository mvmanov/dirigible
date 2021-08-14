/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import './RuntimeInstantiator.js';
import './platform/platform.js';
import './text_utils/text_utils-legacy.js';
import './common/common-legacy.js';
import './heap_snapshot_model/heap_snapshot_model-legacy.js';
import './heap_snapshot_worker/heap_snapshot_worker-legacy.js';

import {startWorker} from './RuntimeInstantiator.js';

startWorker('heap_snapshot_worker_entrypoint');