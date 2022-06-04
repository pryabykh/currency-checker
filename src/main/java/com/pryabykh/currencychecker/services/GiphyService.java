package com.pryabykh.currencychecker.services;

import com.pryabykh.currencychecker.exceptions.GifDataIsNullException;
import com.pryabykh.currencychecker.exceptions.RandomGifIdIsNullException;

public interface GiphyService {
    byte[] fetchRichGif() throws RandomGifIdIsNullException, GifDataIsNullException;
    byte[] fetchBrokeGif() throws RandomGifIdIsNullException, GifDataIsNullException;
}
