# Notification in Android

# 알림 - Notification
- 상태 바는 화면 상단의 한 줄을 의미하며 이곳에 배터리, 네트워크, 시간 등 시스템의 상태 정보가 출력됨.
- 이 상태 바에 앱의 정보를 출력하는 것을 알림이라고 함
- 상태 바는 시스템의 관리 하에 있으므로, 사용자의 앱은 시스템에 의뢰를 하는 방식으로 알림을 출력함.

## 알림 API
- 알림은 NotificationManager 의 notify() 함수로 발생.
- notify() 함수는 NotificationCompat.Builder 가 만들어 주는 Notification 객체를 대입하며,
이 객체는 알림 정보가 저장됨.
- NotificationCompat.Builder 는 NotificationChannel 정보를 대입을 해주어야함.

NotificationChannel -> NotificationCompat.Builder() -> Notification 객체 
-> notify() -> NotificationManager

## 채널
* API 레벨 26 이상에서 추가된 개념으로 앱의 알림을 채널로 구분하겠다는 의도
* 사용자가 환경 설정에서 어떤 앱의 알림을 받을지 말지 설정할 수 있음
* `NotificationChannel(id: String!, name: CharSequence!, importance: Int)`

매개변수로는 채널의 식별값 `id`와 설정 화면에 표시할 채널 이름을 문자열로 저장 `name`, 그리고 알림의 중요도로 나뉜다. 
`importance`

1. NotificationManager.IMPORTANCE_HIGH = 긴급 상황으로 알림음이 울리며 헤드업으로 표시
2. NotificationManager.IMPORTANCE_DEFAULT = 높은 중요도이며 알림음이 울림
3. NotificationManager.IMPORTANCE_LOW = 중간 중요도이며 알림음이 울리지 않음
4. NotificationManager.IMPORTANCE_MIN = 너무 낮아 상태 바에서 조차 표기되지 않음

## 각종 정보 설정
* `fun setDescription(description: String!)` : 채널의 설명 문자열
* `fun setShowBadge(showBadge: Boolean)` : 알림 뱃지 띄우기
* `fun setSound(sound: Uri!, audioAttributes: AudioAttributes!)` : 알림음 재생
* `fun enableLights(lights: Boolean)` : 불빛 표시 여부
* `fun setLightColor(argb: Int)` : 불빛이 표시될 때의 색상
* `fun enableVibration(vibration: Boolean)` : 진동을 울릴 지 여부
* `fun setVibrationPattern(vibrationPattern: LongArray!)` : 진동 패턴을 지정

## 알림 객체
알림 빌더를 만든 후, 이 빌더를 이용해 Notification 객체를 만들어야 한다.
이 객체에 출력할 이미지, 문자열 등의 정보를 담는데, 상태 바에 조그맣게 뜨는 이미지를 Small Icon이라고 부른다.

다음과 같은 정보를 설정하는 것이 가능하다.
```kotlin
builder.setSmallIcon('drawable icons') 
builder.setWhen('sets the current time')
builder.setContentTitle('sets title')
builder.setContentText('Content Message')
```

최종적으로 다음과 같은 과정으로 출력을 한다.
`manager.notify(11, builder.build())`

첫 번째 매개변수는 임의의 변수이며, 이는 프로그래머가 임의로 설정하는 것이 가능하다.
`cancel()` 함수를 호출할 때 한번 더 호출된다.

## 알림 지우기
사용자가 알림을 터치하면 이벤트가 발생할 수 있으며, 이때 알림은 화면에서 자동으로 사라짐.
또한 사용자가 알림을 손가락으로 밀어서 취소할 수도 있음. 만약 이를 원치 않을 시 다음과 같이 지정해야함. 
`builder.setAutoCancel(false)`
`builder.setOngoing(true)`
*** 단, 이 경우 알림을 지울려면 앱에서 따로 무조건 cancel() 함수를 호출하여 지워주어야 함. 안 그럼...계속 남겠지? ***


## 알림 구성
알림은 사용자에게 앱의 상태를 간단하게 알려 주는 기능을 하는데, 사용자가 더 많은 정보를 요구할 수 있음.
그래서 대부분 앱은 사용자가 알림을 터치했을 때 앱의 액티비티 화면을 실행함. 이때 알림 터치 이벤트가 필요함

### 알림 터치 이벤트
알림은 앱이 관활하는 화면이 아니며 시스템에서 관리하는 상태 바에 출력하는 정보라 `onTouchEvent()` 함수는 작동되지 않음.
따라서 Notification 객체에 알림을 터치했을 때 실행해야 하는 정보를 담아주고, 실제 이벤트가 발생 시, 시스템이 거기서 알아서
잘 꺼내 쓰는 방식으로 동작함.

이를 위해 Intent (앱의 컴포넌트를 실행하는 데 필요한 정보) 를 사용해야함.
인텐트는 앱의 코드에서 준비하지만 실제 컴포넌트를 실행하는 시점은 앱에서 정할 수 없기 때문에,
인텐트를 준비한 후 Notification 객체에 담아서 이벤트가 이벤트가 발생할 때 인텐트를 실행해 달라고 요청해야한다.

이러한 의뢰는 `PendingIntent` 클래스를 이용하며 이는 컴포넌트별로 실행을 의뢰하는 함수를 제공함.
```kotlin
static fun getActivity(context, requestCode, intent, flags)
static fun getBroadcast(context, requestCode, intent, flags)
static fun getService(context, requestCode, intent, flags)
```

flag는 int형 상수로, 똑같은 알림이 발생할 때의 동작을 지정한다.
FLAG_IMMUTABLE, FLAG_MUTABLE, FLAG_NO_CREATE, FLAG_ONE_SHOW,
FLAG_UPDATE_CURRENT 가운데 하나를 지정한다. (단, 31이 대상일 시 MUTABLE, IMMUTABLE 중 하나)

다음은 `builder.setContentIntent()` 함수로 터치 이벤트를 등록하여 준다.

### 액션 이벤트
액션은 사용자 이벤트 처리를 목적으로 터치할 대 실행할 인텐트 정보를 PendingIntent 로 구성해서 등록해야 한다. 
실제 사용자가 액션을 터치하면 등록된 인텐트가 시스템에서 실행되어 이벤트가 처리되는 구조이다.

다음 함수를 통해 등록한다.
```open fun addAction(action: Notification.Action!)```

매개변수로 액션의 정보를 담는 Action 객체를 전달한다. Action은 Action.Builder로 제작한다.
Builder(icon: Int, title: CharSequence!, intent: PendingIntent!)

### 원격 입력 (>= API 20)
알림에서 사용자 입력을 직접 받는 기법으로 간단한 입력은 앱의 화면을 이용하지 않고, 원격으로 액션에서
직접 받는 방법이 있다. 

`RemoteInput` 로 실행한다.
RemoteInput.Builder 에 정보를 설정한 후 RemoteInput 객체를 생성함. 빌더는 입력을 식별하는 값, setLabel은
입력칸에 출력되는 힌트 문자열을 지정한다. 당연하지만, 이도 액션 중 하나라, PendingIntent를 준비해야한다.

알림에서 사용자 입력을 받을 대는 브로드캐스트 리시버로 처리한다.
```kotlin
val replyTxt = RemoteInput.getResultsFromIntent(intent)?.getCharSequence("아까 빌더에 들어간 식별값")
```

아래의 코드를 작성하여 글을 잘 받았다는 신호를 보내야한다.

```manager.notify(11, builder.build())```

