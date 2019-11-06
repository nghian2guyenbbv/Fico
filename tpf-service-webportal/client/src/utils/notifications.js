export function pushNotify(header, body, icon, data) {
  var options = {
    body: body,
    // icon: icon || '',
    // data: data || ''
  }

  if (!("Notification" in window)) {
    alert("This browser does not support desktop notification");
  }
  else if (Notification.permission === "granted") {
    var n = new Notification(header,options);
    setTimeout(n.close.bind(n), 4000);
  }
  else if (Notification.permission !== 'denied') {
    Notification.requestPermission(function (permission) {
      if (!('permission' in Notification)) {
        Notification.permission = permission;
      }
      if (permission === "granted") {
        var n = new Notification(header,options);
        setTimeout(n.close.bind(n), 4000);
      }

    
    });
  }
}