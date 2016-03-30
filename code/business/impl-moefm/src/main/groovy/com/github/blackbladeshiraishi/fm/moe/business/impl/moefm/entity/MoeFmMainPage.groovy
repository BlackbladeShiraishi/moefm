package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm.entity

import com.github.blackbladeshiraishi.fm.moe.business.api.entity.MainPage
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Album
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio

public class MoeFmMainPage implements MainPage {

  List<Radio> hotRadios

  List<Album> newAlbums

  List<Album> hotAlbums

  List<Album> albums

}
