'use strict';
var name;
var username = null;
var stompClient = null;


var messageInput=document.querySelector('#messageInput');
var devicesList=document.querySelector('#deviceChatList');
var messageList=document.querySelector('#messageList');
var chatSection = document.querySelector('#ChatSection');
var loginSection= document.querySelector('#loginSection');
function login(){
	  var userName = document.querySelector('#inputEmail').value.trim();
	  var passWord = document.querySelector('#inputPassword').value.trim();
	  var userOject={
	      "username": userName,
	      "password": passWord
	  };
	  var token="";
	  userOject=JSON.stringify(userOject);
	  var xhr = new XMLHttpRequest();
	  var url = "/auth";
	  xhr.open("POST", url, true);
	  xhr.setRequestHeader("Content-Type", "application/json");
	  xhr.send(userOject);
	  xhr.onreadystatechange = function () {
	      if (xhr.readyState === 4 && xhr.status === 200) {
		          var json = JSON.parse(xhr.responseText);
		          token =json.token;
		          sessionStorage.setItem("token", token);
			      var xhr2 = new XMLHttpRequest();
			      var url = "/getUser";
			      xhr2.open("GET", url, true);
			      xhr2.setRequestHeader("token",token);
			      xhr2.send();
			      xhr2.onreadystatechange = function () {
			    	  if (xhr2.readyState === 4 && xhr2.status === 200) {
			    		  var json = JSON.parse(xhr2.responseText);
			    		  name=json.firstName;
			    		  connect(event,name);
			    		  sessionStorage.setItem("loggedin",true);
			    		  loginSection.style.display = "none";
			    		  chatSection.style.display = "block";
			    		  var xhr3 = new XMLHttpRequest();
			    		  var url = "/getallDevices";
			    		  xhr3.open("GET", url, true);
			    		  xhr3.setRequestHeader("token",token);
			    		  xhr3.send();
			    		  xhr3.onreadystatechange = function () {
			    			  if (xhr3.readyState === 4 && xhr3.status === 200) {
			    				  var json = JSON.parse(xhr3.responseText);
			    				  var devices=json.data;
		    					  console.log(devices);

			    				  var device={
			    						  name: "Robot",
			    						  createdTime : new Date(),
			    				          additionalInfo :{description: "platform robot !"}
			    				  }
			    				  createChatListDevicesItem(device);
			    				  for(var counter in devices){
			    					  
			    					  var device2= devices[counter];
			    					  console.log(device2);
			    					  createChatListDevicesItem(device2);
			    				  }
			    				  
 
			    			  }
			    		  }
			    	  }
			      }

	      }
	  };
	}


/*
<div class="chat_list">
       <div class="chat_people">
         <div class="chat_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>
         <div class="chat_ib">
           <h5>Sunil Rajput <span class="chat_date">Dec 25</span></h5>
           <p>Test, which is a new approach to have all solutions 
             astrology under one roof.</p>
         </div>
       </div>
     </div>
     
*/


function createChatListDevicesItem(device){
	 var newlink = document.createElement('LI');
	 newlink.id=device.name;
	 var chatListItem = document.createElement('div');
	 chatListItem.className = 'chat_list';
     var chat_people = document.createElement('div');
     chat_people.className = 'chat_people';
     
     var chat_img = document.createElement('div');
     chat_img.className = 'chat_img';
     var imageItem = document.createElement("IMG");
	 imageItem.setAttribute("src", "/img/myfirst.jpg");
	 imageItem.setAttribute("alt", "User Avatar");
	 chat_img.appendChild(imageItem);
	 chat_people.appendChild(chat_img);
	 
	 var chat_ib = document.createElement('div');
	 chat_ib.className = 'chat_ib';
	 var h5 = document.createElement("H5")                
	 var h5_text = document.createTextNode(device.name);
	 h5.appendChild(h5_text);
	 console.log(device.name);
	 var spanItem =document.createElement('span');
     spanItem.className="chat_date";
     var d = new Date(device.createdTime);
	 console.log(device.createdTime);

     var day = d.getDay();
     var month=d.getMonth();
     spanItem.textContent = day+" / "+month;
     h5.appendChild(spanItem);
     chat_ib.appendChild(h5);
     var description = document.createElement('p');
     if(device.additionalInfo!=null){
    	 var description_text = document.createTextNode(device.additionalInfo.description);
     }else{
    	 var description_text = document.createTextNode("");
     }
     description.appendChild(description_text);

     chat_ib.appendChild(description);
	 chat_people.appendChild(chat_ib);
	 chatListItem.appendChild(chat_people);
	 newlink.appendChild(chatListItem)
	 
	 devicesList.appendChild(newlink)
	 
}


/*
 * 
              <div class="incoming_msg_img"> 
              <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>
              <div class="received_msg">
                <div class="received_withd_msg">
                  <p>We work directly with our designers and suppliers,
                    and sell direct to you, which means quality, exclusive
                    products, at a price anyone can afford.</p>
                  <span class="time_date"> 11:01 AM    |    Today</span></div>
              </div>
            */

function incomeMessageTemplate(messageText){
	 var newlink = document.createElement('LI');
	 
	
	 var messageMainDiv = document.createElement('div');
	 messageMainDiv.className = 'incoming_msg';
	 
	
	 var incomeMessage_img = document.createElement('div');
	 incomeMessage_img.className = 'incoming_msg_img';
     var imageItem = document.createElement("IMG");
	 imageItem.setAttribute("src", "/img/myfirst.jpg");
	 imageItem.setAttribute("alt", "User Avatar");
	 incomeMessage_img.appendChild(imageItem);
	 
	 
	 messageMainDiv.appendChild(incomeMessage_img);
	 
	 var received_msg = document.createElement('div');
	 received_msg.className = 'received_msg';

	 var received_withd_msg = document.createElement('div');
	 received_withd_msg.className = 'received_withd_msg';
	 var message = document.createElement('p');
     var message_text = document.createTextNode(messageText);
     message.appendChild(message_text);
     received_withd_msg.appendChild(message);
     var timeSpan = document.createElement("SPAN");
     timeSpan.className="time_date";
     var d = new Date();
     var hours=d.getHours();
     var min=d.getMinutes();
     var date_text = document.createTextNode(hours+":"+min);
     console.log(date_text);
     timeSpan.appendChild(date_text);
     received_withd_msg.appendChild(timeSpan);

     received_msg.appendChild(received_withd_msg);
     
     messageMainDiv.appendChild(received_msg);
     
     //	</div>

     newlink.appendChild(messageMainDiv);
	 
     messageList.appendChild(newlink);
     
	 
}
/*
  <div class="outgoing_msg">
    <div class="sent_msg">
      <p>Test which is a new approach to have all
           solutions</p>
      <span class="time_date"> 11:01 AM    |    June 9</span>
       </div>
  </div>
            */

function outComeMessageTemplate(messageText){
	
	 var newlink = document.createElement('LI');
	 var outgoing_msg = document.createElement('div');
	 outgoing_msg.className = 'outgoing_msg';
	 
	 var sent_msg = document.createElement('div');
	 sent_msg.className = 'sent_msg';
	 
	 var message = document.createElement('p');
     var message_text = document.createTextNode(messageText);
     message.appendChild(message_text);
     
     var timeSpan = document.createElement("SPAN");
     timeSpan.className="time_date";
     var d = new Date();
     var hours=d.getHours();
     var min=d.getMinutes();
     var date_text = document.createTextNode(hours+":"+min);
     timeSpan.appendChild(date_text);
     
     sent_msg.appendChild(message);
     sent_msg.appendChild(timeSpan);
     
     outgoing_msg.appendChild(sent_msg);
     
     newlink.appendChild(outgoing_msg);
     messageList.appendChild(newlink);
}

function connect(event,name) {
    username =name ;

    if(username) {
        sessionStorage.setItem("machineName","Robot")
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}



function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    
}


function onError(error) {
//    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//    connectingElement.style.color = 'red';
	
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    //event.preventDefault();
}


function onMessageReceived(payload) {

    var message = JSON.parse(payload.body);
    if(message.type === 'JOIN') {
        message.content = message.sender + ' joined!';
        outComeMessageTemplate(message.content);
    } else if (message.type === 'LEAVE') {
        message.content = message.sender + ' left!';
        outComeMessageTemplate(message.content);
    } else {
    	console.log("here");
        outComeMessageTemplate(message.content);
    }

     if(message.type !== 'JOIN'){
	      messageInput.placeholder="waiting for response";
	      setTimeout(function() {
		      messageInput.placeholder="Type a message";
		      var xhr2 = new XMLHttpRequest();
		      var url = "/chatRobot";
		      xhr2.open("POST", url, true);
		      xhr2.setRequestHeader("token", sessionStorage.getItem("token"))
		      xhr2.setRequestHeader("machineName", sessionStorage.getItem("machineName"))
		      xhr2.send(payload.body);
		      xhr2.onreadystatechange = function () {
			      if (xhr2.readyState === 4 && xhr2.status === 200) {
			    	 var robotMessage = JSON.parse(xhr2.responseText);
			    	 incomeMessageTemplate(robotMessage.message);
			    	 
			      }
		      }
	    },2000);
    }
    

}
function downloadFile(){
	console.log();
    var xhr3 = new XMLHttpRequest();
     var url = "/getFile";
     xhr3.open("GET", url, true);
     xhr3.setRequestHeader("fileName",filename);
     xhr3.setRequestHeader("username",name);

     xhr3.send();
    xhr3.onreadystatechange = function () {
       if (xhr3.readyState === 4 && xhr3.status === 200) {
     	 
     		 var blob = xhr3.responseText;
	        if(window.navigator.msSaveOrOpenBlob) {
	            window.navigator.msSaveBlob(blob, filename);
	        }
	        else{
	            var downloadLink = window.document.createElement('a');
	            var contentTypeHeader = xhr3.getResponseHeader("Content-Type");
	            downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
	            downloadLink.download = filename;
	            document.body.appendChild(downloadLink);
	            downloadLink.click();
	            document.body.removeChild(downloadLink);
	         }
     	
     	 }
    }
    
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function logout(){
	sessionStorage.setItem("loggedin",false);
}
