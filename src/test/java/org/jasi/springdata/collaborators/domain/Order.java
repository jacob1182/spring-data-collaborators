/*
 * Copyright 2013-2015 the original author or authors.
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
package org.jasi.springdata.collaborators.domain;

import org.jasi.springdata.collaborators.annotation.Collaborator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Order entity
 *
 * @author @jacob1182
 */
@Document
public class Order {

    @Id
    private String id;

    private String name;

    @Collaborator
    private NotificationService notificationService;

    @Collaborator
    @Qualifier("jsonStoreInfoService")
    private StoreInfoService storeInfo;


    public static Order of(String name) {
        Order product = new Order();
        product.setName(name);
        return product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public StoreInfoService getStoreInfo() {
        return storeInfo;
    }
}
