package com.ctrl.life

import android.content.Context

object CloudSync {
 fun reconcile(context:Context,local:List<CtrlTask>):List<CtrlTask>{
  val session=CloudAuth.session(context)?:return local
  CloudDevice.register(context,session)
  val merged=CloudMerge.merge(local,CloudTasks.pull(context,session))
  CloudTasks.push(context,session,merged)
  return merged
 }
}
