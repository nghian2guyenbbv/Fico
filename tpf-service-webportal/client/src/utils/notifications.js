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
    var notification = new Notification(header, options);
  }
  else if (Notification.permission !== 'denied') {
    Notification.requestPermission(function (permission) {
      if (!('permission' in Notification)) {
        Notification.permission = permission;
      }
      if (permission === "granted") {
        var notification = new Notification(header, options);
      }
    });
  }
}