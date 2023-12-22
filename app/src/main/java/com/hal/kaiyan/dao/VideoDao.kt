package com.hal.kaiyan.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hal.kaiyan.entity.VideoInfoData


/**
 * ...
 * @author LeiHao
 * @date 2023/12/13
 * @description 视频的数据库访问对象
 */

@Dao
interface VideoDao {
    @Insert
    fun insertVideos(vararg videoInfoData: VideoInfoData)

    @Delete
    fun deleteVideos(vararg videoInfoData: VideoInfoData)

    //查询有没有某一条视频
    @Query("SELECT COUNT(*) FROM video_tb WHERE id = :id")
    fun hasVideo(id: Int): LiveData<Int>

    /**
     * 本地分页查询
     */
    @Query("SELECT * FROM video_tb ORDER BY collect_date DESC")
    fun getVideos(): PagingSource<Int, VideoInfoData>

    /**
     * 删除全部收藏
     */
    @Query("DELETE FROM video_tb")
    fun deleteAllVideos()

    /**
     * 查看数据库中是否有数据
     */
    @Query("SELECT COUNT(*) FROM video_tb")
    fun hasVideoCount(): LiveData<Int>

}