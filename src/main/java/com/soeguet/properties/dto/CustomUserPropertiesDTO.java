package com.soeguet.properties.dto;

public record CustomUserPropertiesDTO(String username, String nickname, String borderColor) {

    public int getBorderColor() {

        return borderColor == null ? -1 : Integer.parseInt(borderColor);
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof CustomUserPropertiesDTO customUserPropertiesDTO) {

            return username.equals(customUserPropertiesDTO.username);
        }

        return false;
    }
}