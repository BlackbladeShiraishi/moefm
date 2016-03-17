import org.gradle.api.GradleException

import java.text.SimpleDateFormat

class Version {

  String originalVersion
  String versionName
  int versionCode
  String status
  Date buildTime

  Version(String versionValue) {
    if (versionValue =~ /\d{2,}/) {
      throw new GradleException("versionValue(${versionValue}) contains number more than 9.")
    }
    buildTime = new Date()
    originalVersion = versionValue
    String originalVersionCodeString;
    if (originalVersion.endsWith('-SNAPSHOT')) {
      status = 'integration'
      originalVersionCodeString = originalVersion.
          substring(0, originalVersion.length() - '-SNAPSHOT'.length())
      versionName = originalVersionCodeString + '-' + getTimestamp()
    } else {
      status = 'release'
      versionName = versionValue
      originalVersionCodeString = versionValue
    }
    versionCode = originalVersionCodeString.replace('.', '').toInteger();
  }

  String getTimestamp() {
    // Convert local file timestamp to UTC
    def format = new SimpleDateFormat('yyyyMMddHHmmss')
    format.setCalendar(Calendar.getInstance(TimeZone.getTimeZone('UTC')));
    return format.format(buildTime)
  }

  String toString() {
    versionName
  }
}
