package com.yagodaoud.comandae.utils;

import com.yagodaoud.comandae.model.BitcoinNetwork;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BitcoinQRGenerator {

    private static final BigDecimal NETWORK_FEE_PERCENTAGE = new BigDecimal("0.001");
    private static final String SATOSHI_URI_PREFIX = "bitcoin:";
    private static final String LIGHTNING_URI_PREFIX = "lightning:";

    public static String generateBitcoinQR(String address, BigDecimal amountInBRL,
                                           BigDecimal btcPriceInBRL, BitcoinNetwork networkType) {

        BigDecimal btcAmount = amountInBRL.divide(btcPriceInBRL, 8, RoundingMode.HALF_UP);

        BigDecimal feeAmount = btcAmount.multiply(NETWORK_FEE_PERCENTAGE);
        BigDecimal totalAmount = btcAmount.add(feeAmount);

        return switch (networkType) {
            case MAINNET -> generateOnChainQR(address, totalAmount);
            case LIGHTNING -> generateLightningQR(address, totalAmount);
        };
    }

    private static String generateOnChainQR(String address, BigDecimal btcAmount) {
        return SATOSHI_URI_PREFIX +
                address +
                "?amount=" +
                btcAmount.stripTrailingZeros().toPlainString();
    }

    private static String generateLightningQR(String invoice, BigDecimal btcAmount) {
        return LIGHTNING_URI_PREFIX +
                URLEncoder.encode(invoice, StandardCharsets.UTF_8);
    }

    public static BigDecimal satoshisToBTC(long satoshis) {
        return BigDecimal.valueOf(satoshis).divide(BigDecimal.valueOf(100_000_000), 8, RoundingMode.HALF_UP);
    }

    public static long btcToSatoshis(BigDecimal btc) {
        return btc.multiply(BigDecimal.valueOf(100_000_000)).longValue();
    }
}
