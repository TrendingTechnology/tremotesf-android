/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class JniRpc extends BaseRpc {
  private transient long swigCPtr;

  protected JniRpc(long cPtr, boolean cMemoryOwn) {
    super(libtremotesfJNI.JniRpc_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(JniRpc obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtremotesfJNI.delete_JniRpc(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    libtremotesfJNI.JniRpc_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    libtremotesfJNI.JniRpc_change_ownership(this, swigCPtr, true);
  }

  public JniRpc() {
    this(libtremotesfJNI.new_JniRpc(), true);
    libtremotesfJNI.JniRpc_director_connect(this, swigCPtr, true, true);
  }

  public JniServerSettings serverSettings() {
    long cPtr = libtremotesfJNI.JniRpc_serverSettings(swigCPtr, this);
    return (cPtr == 0) ? null : new JniServerSettings(cPtr, false);
  }

  public void setServer(String name, String address, int port, String apiPath, boolean https, boolean selfSignedCertificateEnabled, byte[] selfSignedCertificate, boolean clientCertificateEnabled, byte[] clientCertificate, boolean authentication, String username, String password, int updateInterval, int backgroundUpdateInterval, int timeout) {
    libtremotesfJNI.JniRpc_setServer(swigCPtr, this, name, address, port, apiPath, https, selfSignedCertificateEnabled, selfSignedCertificate, clientCertificateEnabled, clientCertificate, authentication, username, password, updateInterval, backgroundUpdateInterval, timeout);
  }

  public void resetServer() {
    libtremotesfJNI.JniRpc_resetServer(swigCPtr, this);
  }

  public void connect() {
    libtremotesfJNI.JniRpc_connect(swigCPtr, this);
  }

  public void disconnect() {
    libtremotesfJNI.JniRpc_disconnect(swigCPtr, this);
  }

  public void setBackgroundUpdate(boolean background) {
    libtremotesfJNI.JniRpc_setBackgroundUpdate(swigCPtr, this, background);
  }

  public void setUpdateDisabled(boolean disabled) {
    libtremotesfJNI.JniRpc_setUpdateDisabled(swigCPtr, this, disabled);
  }

  public void addTorrentFile(byte[] fileData, String downloadDirectory, int[] wantedFiles, int[] unwantedFiles, int[] highPriorityFiles, int[] normalPriorityFiles, int[] lowPriorityFiles, int bandwidthPriority, boolean start) {
    libtremotesfJNI.JniRpc_addTorrentFile(swigCPtr, this, fileData, downloadDirectory, wantedFiles, unwantedFiles, highPriorityFiles, normalPriorityFiles, lowPriorityFiles, bandwidthPriority, start);
  }

  public void addTorrentLink(String link, String downloadDirectory, int bandwidthPriority, boolean start) {
    libtremotesfJNI.JniRpc_addTorrentLink(swigCPtr, this, link, downloadDirectory, bandwidthPriority, start);
  }

  public void startTorrents(int[] ids) {
    libtremotesfJNI.JniRpc_startTorrents(swigCPtr, this, ids);
  }

  public void startTorrentsNow(int[] ids) {
    libtremotesfJNI.JniRpc_startTorrentsNow(swigCPtr, this, ids);
  }

  public void pauseTorrents(int[] ids) {
    libtremotesfJNI.JniRpc_pauseTorrents(swigCPtr, this, ids);
  }

  public void removeTorrents(int[] ids, boolean deleteFiles) {
    libtremotesfJNI.JniRpc_removeTorrents(swigCPtr, this, ids, deleteFiles);
  }

  public void checkTorrents(int[] ids) {
    libtremotesfJNI.JniRpc_checkTorrents(swigCPtr, this, ids);
  }

  public void moveTorrentsToTop(int[] ids) {
    libtremotesfJNI.JniRpc_moveTorrentsToTop(swigCPtr, this, ids);
  }

  public void moveTorrentsUp(int[] ids) {
    libtremotesfJNI.JniRpc_moveTorrentsUp(swigCPtr, this, ids);
  }

  public void moveTorrentsDown(int[] ids) {
    libtremotesfJNI.JniRpc_moveTorrentsDown(swigCPtr, this, ids);
  }

  public void moveTorrentsToBottom(int[] ids) {
    libtremotesfJNI.JniRpc_moveTorrentsToBottom(swigCPtr, this, ids);
  }

  public void reannounceTorrents(int[] ids) {
    libtremotesfJNI.JniRpc_reannounceTorrents(swigCPtr, this, ids);
  }

  public void setTorrentsLocation(int[] ids, String location, boolean moveFiles) {
    libtremotesfJNI.JniRpc_setTorrentsLocation(swigCPtr, this, ids, location, moveFiles);
  }

  public void getTorrentFiles(int id, boolean scheduled) {
    libtremotesfJNI.JniRpc_getTorrentFiles(swigCPtr, this, id, scheduled);
  }

  public void getTorrentPeers(int id, boolean scheduled) {
    libtremotesfJNI.JniRpc_getTorrentPeers(swigCPtr, this, id, scheduled);
  }

  public void renameTorrentFile(int torrentId, String filePath, String newName) {
    libtremotesfJNI.JniRpc_renameTorrentFile(swigCPtr, this, torrentId, filePath, newName);
  }

  public void getDownloadDirFreeSpace() {
    libtremotesfJNI.JniRpc_getDownloadDirFreeSpace(swigCPtr, this);
  }

  public void getFreeSpaceForPath(String path) {
    libtremotesfJNI.JniRpc_getFreeSpaceForPath(swigCPtr, this, path);
  }

  public void setTorrentDownloadSpeedLimited(Torrent torrent, boolean limited) {
    libtremotesfJNI.JniRpc_setTorrentDownloadSpeedLimited(swigCPtr, this, Torrent.getCPtr(torrent), torrent, limited);
  }

  public void setTorrentDownloadSpeedLimit(Torrent torrent, int limit) {
    libtremotesfJNI.JniRpc_setTorrentDownloadSpeedLimit(swigCPtr, this, Torrent.getCPtr(torrent), torrent, limit);
  }

  public void setTorrentUploadSpeedLimited(Torrent torrent, boolean limited) {
    libtremotesfJNI.JniRpc_setTorrentUploadSpeedLimited(swigCPtr, this, Torrent.getCPtr(torrent), torrent, limited);
  }

  public void setTorrentUploadSpeedLimit(Torrent torrent, int limit) {
    libtremotesfJNI.JniRpc_setTorrentUploadSpeedLimit(swigCPtr, this, Torrent.getCPtr(torrent), torrent, limit);
  }

  public void setTorrentRatioLimitMode(Torrent torrent, int mode) {
    libtremotesfJNI.JniRpc_setTorrentRatioLimitMode(swigCPtr, this, Torrent.getCPtr(torrent), torrent, mode);
  }

  public void setTorrentRatioLimit(Torrent torrent, double limit) {
    libtremotesfJNI.JniRpc_setTorrentRatioLimit(swigCPtr, this, Torrent.getCPtr(torrent), torrent, limit);
  }

  public void setTorrentPeersLimit(Torrent torrent, int limit) {
    libtremotesfJNI.JniRpc_setTorrentPeersLimit(swigCPtr, this, Torrent.getCPtr(torrent), torrent, limit);
  }

  public void setTorrentHonorSessionLimits(Torrent torrent, boolean honor) {
    libtremotesfJNI.JniRpc_setTorrentHonorSessionLimits(swigCPtr, this, Torrent.getCPtr(torrent), torrent, honor);
  }

  public void setTorrentBandwidthPriority(Torrent torrent, int priority) {
    libtremotesfJNI.JniRpc_setTorrentBandwidthPriority(swigCPtr, this, Torrent.getCPtr(torrent), torrent, priority);
  }

  public void setTorrentIdleSeedingLimitMode(Torrent torrent, int mode) {
    libtremotesfJNI.JniRpc_setTorrentIdleSeedingLimitMode(swigCPtr, this, Torrent.getCPtr(torrent), torrent, mode);
  }

  public void setTorrentIdleSeedingLimit(Torrent torrent, int limit) {
    libtremotesfJNI.JniRpc_setTorrentIdleSeedingLimit(swigCPtr, this, Torrent.getCPtr(torrent), torrent, limit);
  }

  public void setTorrentFilesEnabled(Torrent torrent, boolean enabled) {
    libtremotesfJNI.JniRpc_setTorrentFilesEnabled(swigCPtr, this, Torrent.getCPtr(torrent), torrent, enabled);
  }

  public void setTorrentFilesWanted(Torrent torrent, int[] files, boolean wanted) {
    libtremotesfJNI.JniRpc_setTorrentFilesWanted(swigCPtr, this, Torrent.getCPtr(torrent), torrent, files, wanted);
  }

  public void setTorrentFilesPriority(Torrent torrent, int[] files, int priority) {
    libtremotesfJNI.JniRpc_setTorrentFilesPriority(swigCPtr, this, Torrent.getCPtr(torrent), torrent, files, priority);
  }

  public void torrentRenameFile(Torrent torrent, String path, String newName) {
    libtremotesfJNI.JniRpc_torrentRenameFile(swigCPtr, this, Torrent.getCPtr(torrent), torrent, path, newName);
  }

  public void torrentAddTracker(Torrent torrent, String announce) {
    libtremotesfJNI.JniRpc_torrentAddTracker(swigCPtr, this, Torrent.getCPtr(torrent), torrent, announce);
  }

  public void torrentSetTracker(Torrent torrent, int trackerId, String announce) {
    libtremotesfJNI.JniRpc_torrentSetTracker(swigCPtr, this, Torrent.getCPtr(torrent), torrent, trackerId, announce);
  }

  public void torrentRemoveTrackers(Torrent torrent, int[] ids) {
    libtremotesfJNI.JniRpc_torrentRemoveTrackers(swigCPtr, this, Torrent.getCPtr(torrent), torrent, ids);
  }

  public void setTorrentPeersEnabled(Torrent torrent, boolean enabled) {
    libtremotesfJNI.JniRpc_setTorrentPeersEnabled(swigCPtr, this, Torrent.getCPtr(torrent), torrent, enabled);
  }

  protected void onAboutToDisconnect() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onAboutToDisconnect(swigCPtr, this); else libtremotesfJNI.JniRpc_onAboutToDisconnectSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onConnectedChanged() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onConnectedChanged(swigCPtr, this); else libtremotesfJNI.JniRpc_onConnectedChangedSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onStatusChanged() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onStatusChanged(swigCPtr, this); else libtremotesfJNI.JniRpc_onStatusChangedSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onErrorChanged() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onErrorChanged(swigCPtr, this); else libtremotesfJNI.JniRpc_onErrorChangedSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onTorrentsUpdated() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onTorrentsUpdated(swigCPtr, this); else libtremotesfJNI.JniRpc_onTorrentsUpdatedSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onServerStatsUpdated() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onServerStatsUpdated(swigCPtr, this); else libtremotesfJNI.JniRpc_onServerStatsUpdatedSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onTorrentAdded(int id, String hashString, String name) {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onTorrentAdded(swigCPtr, this, id, hashString, name); else libtremotesfJNI.JniRpc_onTorrentAddedSwigExplicitJniRpc(swigCPtr, this, id, hashString, name);
  }

  protected void onTorrentFinished(int id, String hashString, String name) {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onTorrentFinished(swigCPtr, this, id, hashString, name); else libtremotesfJNI.JniRpc_onTorrentFinishedSwigExplicitJniRpc(swigCPtr, this, id, hashString, name);
  }

  protected void onTorrentAddDuplicate() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onTorrentAddDuplicate(swigCPtr, this); else libtremotesfJNI.JniRpc_onTorrentAddDuplicateSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onTorrentAddError() {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onTorrentAddError(swigCPtr, this); else libtremotesfJNI.JniRpc_onTorrentAddErrorSwigExplicitJniRpc(swigCPtr, this);
  }

  protected void onGotTorrentFiles(int torrentId) {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onGotTorrentFiles(swigCPtr, this, torrentId); else libtremotesfJNI.JniRpc_onGotTorrentFilesSwigExplicitJniRpc(swigCPtr, this, torrentId);
  }

  protected void onTorrentFileRenamed(int torrentId, String filePath, String newName) {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onTorrentFileRenamed(swigCPtr, this, torrentId, filePath, newName); else libtremotesfJNI.JniRpc_onTorrentFileRenamedSwigExplicitJniRpc(swigCPtr, this, torrentId, filePath, newName);
  }

  protected void onGotTorrentPeers(int torrentId) {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onGotTorrentPeers(swigCPtr, this, torrentId); else libtremotesfJNI.JniRpc_onGotTorrentPeersSwigExplicitJniRpc(swigCPtr, this, torrentId);
  }

  protected void onGotDownloadDirFreeSpace(long bytes) {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onGotDownloadDirFreeSpace(swigCPtr, this, bytes); else libtremotesfJNI.JniRpc_onGotDownloadDirFreeSpaceSwigExplicitJniRpc(swigCPtr, this, bytes);
  }

  protected void onGotFreeSpaceForPath(String path, boolean success, long bytes) {
    if (getClass() == JniRpc.class) libtremotesfJNI.JniRpc_onGotFreeSpaceForPath(swigCPtr, this, path, success, bytes); else libtremotesfJNI.JniRpc_onGotFreeSpaceForPathSwigExplicitJniRpc(swigCPtr, this, path, success, bytes);
  }

}
