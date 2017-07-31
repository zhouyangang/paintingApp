# Tumcca

What is the name for? Inspired by Tumblr, I suggest we name our Chinese Calligraphy and Arts platform as tumcca, while Tumblr is abbreviated for "tumblelogs".

### Running The Application

To test the example application run the following commands.

* To package the example run.

        mvn package -DskipTests

* To run the server run.

        java -jar target/tumcca-0.1.0-SNAPSHOT.jar server tumcca.yml

* To run in background.

        nohup java -Des.config=elasticsearch.yml -Xms256m -Xmx256m -jar tumcca-0.1.0-SNAPSHOT.jar server tumcca.yml >console.log 2>&1 &

### WS

Host: http://120.26.202.114

```
    /ws/follow

    Content-Type: application/json
    Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
    REQUEST:
    {
      "follower": 1,
      "toFollow": 2
    }
    NOTIFY:
    {
      "follower": 1,
      "toFollow": 2
    }
```
```
    /ws/unfollow

    Content-Type: application/json
    Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
    REQUEST:
    {
      "follower": 1,
      "following": 2
    }
    NOTIFY:
    {
      "follower": 1,
      "following": 2
    }
```

### APIs

Host: http://120.26.202.114

```
    POST    /api/email/exists
            Content-Type: application/json
            REQUEST:
            {
              "email": "test@test.test"
            }
            RESPONSE:
            true/false
```
```
    POST    /api/mobile/exists
            Content-Type: application/json
            REQUEST:
            {
              "mobile": "13878889888"
            }
            RESPONSE:
            true/false
```
```
    POST    /api/sign-up
            Content-Type: application/json
            REQUEST:
            {
              "email": "ideaalloc@gmail.com",
              "password": "888888"
            }
            OR
            {
              "mobile": "18771076625",
              "password": "888888"
            }
            RESPONSE:
            {
              "uid": 4
            }
```
```
    POST    /api/artists/profile
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            REQUEST:
            {
              "pseudonym": "令狐冲",
              "gender": 1,
              "introduction": "书画极客",
              "title": "书画协会会员",
              "hobbies": "书画, 健身",
              "forte": "互联网分析",
              "avatar": 0,
              "country": "中国",
              "province": "湖北",
              "city": "武汉"
            }
            RESPONSE:
            {
              "uid": 4
            }
```
```
    GET     /api/artists/profile
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "gender" : 1,
              "introduction" : "书画极客",
              "pseudonym" : "令狐冲",
              "avatar" : 0,
              "hobbies" : "书画, 健身",
              "title" : "书画协会会员",
              "country" : "中国",
              "province" : "湖北",
              "city" : "武汉",
              "forte" : "互联网分析"
            }
```
```
    GET     /api/artists/{uid}/profile eg.: /api/artists/1/profile
            Content-Type: application/json
            RESPONSE:
            {
              "gender" : 1,
              "introduction" : "书画极客",
              "pseudonym" : "令狐冲",
              "avatar" : 0,
              "hobbies" : "书画, 健身",
              "title" : "书画协会会员",
              "country" : "中国",
              "province" : "湖北",
              "city" : "武汉",
              "forte" : "互联网分析"
            }
```
```
    GET     /api/artists/{uid}/contact eg.: /api/artists/1/contact
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "gender" : 1,
              "introduction" : "书画极客",
              "pseudonym" : "令狐冲",
              "avatar" : 0,
              "hobbies" : "书画, 健身",
              "title" : "书画协会会员",
              "country" : "中国",
              "province" : "湖北",
              "city" : "武汉",
              "forte" : "互联网分析"
            }
```
```
    GET     /api/artists/account
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "mobile" : "18771076625",
              "authority" : "ARTIST",
              "email" : "ideaalloc@gmail.com"
            }
```
```
    PUT     /api/artists/account
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            REQUEST:
            {
              "mobile" : "18771076626",
              "email" : "ideaalloc2@gmail.com"
            }
            RESPONSE:
            {
              "uid": 4
            }
```
```
    DELETE  /api/artists/{uid} eg: /api/artists/1
            Content-Type: application/json
```
```
    POST    /api/sign-in
            Content-Type: application/json
            REQUEST:
            {
              "username": "ideaalloc@gmail.com",
              "password": "888888"
            }
            OR
            {
              "username": "18771076625",
              "password": "888888"
            }
            RESPONSE:
            {
              "uid": 4,
              "token": "c8ba84ef-5cd9-4830-851f-0a8024388da7"
            }
```
```
    POST    /api/sign-out
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            REQUEST:
            {
              "uid": 4,
              "token": "c8ba84ef-5cd9-4830-851f-0a8024388da7"
            }
            RESPONSE:
            {
              "uid": 4
            }
```
```
    POST    /api/avatars/upload
            Content-Type: application/json
            PAYLOAD: avatar
            RESPONSE:
            {
              "id": 1
            }
```
```
    GET     /api/avatars/download/{id} eg: /api/avatars/download/1
            Content-Type: application/json
            RESPONSE: file
            ERROR: Id Not found
```
```
    GET     /api/avatars/download/{id}/thumb/{targetWidth}/{targetHeight} eg: /api/avatars/download/1/thumb/150/110
            Content-Type: application/json
            RESPONSE: file
            ERROR: Id Not found
```
```
    POST    /api/pictures/upload
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            PAYLOAD: works
            RESPONSE:
            {
              "id": 1
            }
```
```
    GET     /api/pictures/download/{id} eg: /api/pictures/download/1
            Content-Type: application/json
            RESPONSE: file
            ERROR: Id Not found
```
```
    GET     /api/pictures/download/{id}/thumb/{targetWidth}/{targetHeight} eg: /api/pictures/download/1/thumb/150/110
            Content-Type: application/json
            RESPONSE: file
            ERROR: Id Not found
```
```
    DELETE  /api/pictures/trash/{id} eg: /api/pictures/trash/1
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "id": 1
            }
```
```
    POST    /api/pictures/restore/{id} eg: /api/pictures/restore/1
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "id": 1
            }
```
```
    DELETE  /api/pictures/remove/{id} eg: /api/pictures/remove/1
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "id": 1
            }
```
```
    POST    /api/logs/android
            Content-Type: application/json
            REQUEST:
            {
              "description": "oh shit happens"
            }
            RESPONSE:
            {
              "id": 1
            }
```
```
    GET     /api/logs/android/size/{size} eg: /api/logs/android/size/10
            Content-Type: application/json
            RESPONSE:
            [
              {
                "description": "oh shit happens",
                "ts": "2015-06-06 14:04:41.000"
              }
            ]
```
```
    GET     /api/misc/errors
            Content-Type: application/json
            RESPONSE:
            [
              {
                "code": 1001,
                "message": "username or password do NOT match"
              },
              {
                "code": 1002,
                "message": "Session timeout or you've already signed out"
              },
              {
                "code": 1003,
                "message": "user NOT found"
              },
              {
                "code": 1004,
                "message": "artist NOT found"
              },
              {
                "code": 1005,
                "message": "the email has been registered"
              },
              {
                "code": 1006,
                "message": "the mobile has been registered"
              },
              {
                "code": 1007,
                "message": "An email or mobile should have been input"
              }
            ]
```
```
    POST    /api/tags/exists
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            Authority: ADMIN e.g. login as 18771076625/888888
            REQUEST:
            {
              "name": "颜体"
            }
            RESPONSE: true/false
```
```
    POST    /api/tags
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            Authority: ADMIN e.g. login as 18771076625/888888
            REQUEST:
            {
              "name": "颜体"
            }
            RESPONSE: true/false
```
```
    DELETE  /api/tags/{tagId}
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            Authority: ADMIN e.g. login as 18771076625/888888
            RESPONSE:
            {
              "id": 1
            }
```
```
    GET     /api/tags
            Content-Type: application/json
            RESPONSE:
            [
              {
                "id": 3,
                "name": "小婉"
              },
              {
                "id": 2,
                "name": "柳体"
              },
              {
                "id": 1,
                "name": "颜体"
              }
            ]
```
```
    POST    /api/categories/exists
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            Authority: ADMIN e.g. login as 18771076625/888888
            REQUEST:
            {
              "parentId": 0,
              "name": "绘画"
            }
            RESPONSE: true/false
```
```
    POST    /api/categories
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            Authority: ADMIN e.g. login as 18771076625/888888
            REQUEST:
            {
              "parentId": 0,
              "name": "绘画"
            }
            RESPONSE:
            {
              "id": 1
            }
```
```
    PUT     /api/categories
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            Authority: ADMIN e.g. login as 18771076625/888888
            REQUEST:
            {
              "id": 0,
              "name": "书法"
            }
            RESPONSE:
            {
              "id": 1
            }
```
```
    DELETE  /api/categories/{categoryId}
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            Authority: ADMIN e.g. login as 18771076625/888888
            RESPONSE:
            {
              "id": 1
            }
```
```
    GET     /api/categories/{parentId}
            Content-Type: application/json
            RESPONSE:
            [
              {
                "id": 8,
                "name": "书法"
              },
              {
                "id": 2,
                "name": "绘画"
              }
            ]
```
```
    GET     /api/categories/{parentId}/recursively
            Content-Type: application/json
            RESPONSE:
            [
              {
                "id": 8,
                "name": "书法",
                "children": [
                  {
                    "id": 9,
                    "name": "隶书",
                    "children": []
                  }
                ]
              },
              {
                "id": 2,
                "name": "绘画",
                "children": []
              }
            ]
```
```
    GET     /api/categories/all
            Content-Type: application/json
            RESPONSE:
            [
              {
                "id": 8,
                "name": "书法",
                "children": [
                  {
                    "id": 9,
                    "name": "隶书",
                    "children": []
                  }
                ]
              },
              {
                "id": 2,
                "name": "绘画",
                "children": []
              }
            ]
```
```
    GET     /api/works/page/{page}/size/{size}
            Content-Type: application/json
            RESPONSE:
            [
              {
                "id": 2,
                "category": 9,
                "tags": "string",
                "title": "string",
                "description": "string",
                "author": 3,
                "pictures": [
                  1
                ],
                "createTime": "2015-06-16 22:50:25.000"
              }
            ]
```
```
    GET     /api/works/self/page/{page}/size/{size}
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            [
              {
                "id": 2,
                "category": 9,
                "tags": "string",
                "title": "string",
                "description": "string",
                "author": 3,
                "pictures": [
                  1
                ],
                "createTime": "2015-06-16 22:50:25.000"
              }
            ]
```
```
    GET     /api/works/author/{authorId}/page/{page}/size/{size}
            Content-Type: application/json
            RESPONSE:
            [
              {
                "id": 2,
                "category": 9,
                "tags": "string",
                "title": "string",
                "description": "string",
                "author": 3,
                "pictures": [
                  1
                ],
                "createTime": "2015-06-16 22:50:25.000"
              }
            ]
```
```
    POST    /api/works
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            REQUEST:
            {
              "category": 9,
              "tags": "string",
              "title": "string",
              "description": "string",
              "pictures": [
                1
              ]
            }
            RESPONSE:
            {
              "id": 1
            }
```
```
    PUT     /api/works/{worksId}
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            REQUEST:
            {
              "category": 9,
              "tags": "string",
              "title": "string",
              "description": "string",
              "pictures": [
                1
              ]
            }
            RESPONSE:
            {
              "id": 1
            }
```
```
    DELETE  /api/works/trash/{worksId}
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "id": 1
            }
```
```
    POST    /api/works/restore/{worksId}
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "id": 1
            }
```
```
    DELETE  /api/works/{worksId}
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "id": 1
            }
```
```
    POST    /api/works/search
            Content-Type: application/json
            REQUEST:
            {
              "keywords": "帖 习",
              "page": 1,
              "size": 1
            }
            RESPONSE:
            [
              {
                "id": 1,
                "category": 1,
                "tags": "王羲之 名家",
                "title": "名家临摹",
                "description": "名帖，习作",
                "author": 1,
                "pictures": [
                  1
                ],
                "createTime": "2015-06-17 17:53:30.000"
              }
            ]
```
```
    GET     /api/notifications
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            [
              {
                "action" : "FOLLOW",
                "id" : 2,
                "message" : "{\"follower\":3,\"toFollow\":4}",
                "createTime" : "2015-06-22 20:48:46.000"
              }
            ]
```
```
    POST    /api/notifications/{notificationId}/read
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "id" : 2
            }
```
```
    POST    /api/follow/notify
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            REQUEST:
            {
              "follower": 3,
              "toFollow": 4
            }
            RESPONSE:
            {
              "toFollow": 4
            }
```
```
    POST    /api/unfollow/notify
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            REQUEST:
            {
              "follower": 3,
              "following": 6
            }
            RESPONSE:
            {
              "follower": 3
            }
```
```
    GET     /api/profile/followers/count
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "followerCount": 5
            }
```
```
    GET     /api/profile/{uid}/followers/count
            Content-Type: application/json
            RESPONSE:
            {
              "followerCount": 5
            }
```
```
    GET     /api/profile/following/count
            Content-Type: application/json
            Authorization: Bearer c8ba84ef-5cd9-4830-851f-0a8024388da7
            RESPONSE:
            {
              "followingCount": 5
            }
```
```
    GET     /api/profile/{uid}/following/count
            Content-Type: application/json
            RESPONSE:
            {
              "followingCount": 5
            }
```
```
    GET     /api/photo-info/{photoId} eg: /api/photo-info/1
            Content-Type: application/json
            RESPONSE:
            {
              "id": 1,
              "title": "Hello Tumcca"
            }
            ERROR: Id Not found
```

```
    GET     /api/photos/{photoId} eg: /api/photos/1
            Content-Type: application/json
            RESPONSE: file
            ERROR: Id Not found
```

### References

[Web Client Sample](https://github.com/mgutz/dropwizard-atmosphere/blob/master/app/js/application.js)
[Android Client Sample](https://jfarcand.wordpress.com/2013/04/04/wasync-websockets-with-fallbacks-transports-for-android-node-js-and-atmosphere)
[Atmosphere Header](https://github.com/Atmosphere/atmosphere/wiki/JavaScript-jQuery-API-custom-headers)
