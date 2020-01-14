/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class JniRpc extends Rpc {
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

  public void pauseTorrents(int[] ids) {
    libtremotesfJNI.JniRpc_pauseTorrents(swigCPtr, this, ids);
  }

  public void removeTorrents(int[] ids, boolean deleteFiles) {
    libtremotesfJNI.JniRpc_removeTorrents(swigCPtr, this, ids, deleteFiles);
  }

  public void checkTorrents(int[] ids) {
    libtremotesfJNI.JniRpc_checkTorrents(swigCPtr, this, ids);
  }

  public void reannounceTorrents(int[] ids) {
    libtremotesfJNI.JniRpc_reannounceTorrents(swigCPtr, this, ids);
  }

  public void setTorrentsLocation(int[] ids, String location, boolean moveFiles) {
    libtremotesfJNI.JniRpc_setTorrentsLocation(swigCPtr, this, ids, location, moveFiles);
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

  public void updateData() {
    libtremotesfJNI.JniRpc_updateData(swigCPtr, this);
  }

  protected void onAboutToDisconnect() {
    libtremotesfJNI.JniRpc_onAboutToDisconnect(swigCPtr, this);
  }

  protected void onStatusChanged(int status) {
    libtremotesfJNI.JniRpc_onStatusChanged(swigCPtr, this, status);
  }

  protected void onErrorChanged(int error, String errorMessage) {
    libtremotesfJNI.JniRpc_onErrorChanged(swigCPtr, this, error, errorMessage);
  }

  protected void onTorrentsUpdated(TorrentsVector torrents) {
    libtremotesfJNI.JniRpc_onTorrentsUpdated(swigCPtr, this, TorrentsVector.getCPtr(torrents), torrents);
  }

  protected void onTorrentFilesUpdated(int torrentId, TorrentFilesVector changed) {
    libtremotesfJNI.JniRpc_onTorrentFilesUpdated(swigCPtr, this, torrentId, TorrentFilesVector.getCPtr(changed), changed);
  }

  protected void onTorrentPeersUpdated(int torrentId, TorrentPeersVector changed, TorrentPeersVector added, IntVector removed) {
    libtremotesfJNI.JniRpc_onTorrentPeersUpdated(swigCPtr, this, torrentId, TorrentPeersVector.getCPtr(changed), changed, TorrentPeersVector.getCPtr(added), added, IntVector.getCPtr(removed), removed);
  }

  protected void onServerStatsUpdated() {
    libtremotesfJNI.JniRpc_onServerStatsUpdated(swigCPtr, this);
  }

  protected void onTorrentAdded(int id, String hashString, String name) {
    libtremotesfJNI.JniRpc_onTorrentAdded(swigCPtr, this, id, hashString, name);
  }

  protected void onTorrentFinished(int id, String hashString, String name) {
    libtremotesfJNI.JniRpc_onTorrentFinished(swigCPtr, this, id, hashString, name);
  }

  protected void onTorrentAddDuplicate() {
    libtremotesfJNI.JniRpc_onTorrentAddDuplicate(swigCPtr, this);
  }

  protected void onTorrentAddError() {
    libtremotesfJNI.JniRpc_onTorrentAddError(swigCPtr, this);
  }

  protected void onTorrentFileRenamed(int torrentId, String filePath, String newName) {
    libtremotesfJNI.JniRpc_onTorrentFileRenamed(swigCPtr, this, torrentId, filePath, newName);
  }

  protected void onGotDownloadDirFreeSpace(long bytes) {
    libtremotesfJNI.JniRpc_onGotDownloadDirFreeSpace(swigCPtr, this, bytes);
  }

  protected void onGotFreeSpaceForPath(String path, boolean success, long bytes) {
    libtremotesfJNI.JniRpc_onGotFreeSpaceForPath(swigCPtr, this, path, success, bytes);
  }

}
