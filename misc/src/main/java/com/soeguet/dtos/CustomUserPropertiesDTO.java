package com.soeguet.dtos;

public record CustomUserPropertiesDTO(String username, String nickname, String borderColor) {

  public int getBorderColor() {

    return borderColor == null ? -1 : Integer.parseInt(borderColor);
  }

  public CustomUserPropertiesDTO withUsername(final String updatedUsername) {

    return new CustomUserPropertiesDTO(updatedUsername, nickname, borderColor);
  }

  public CustomUserPropertiesDTO withNickname(final String updatedNickname) {

    return new CustomUserPropertiesDTO(username, updatedNickname, borderColor);
  }

  public CustomUserPropertiesDTO withBorderColor(final String updatedBorderColor) {

    return new CustomUserPropertiesDTO(username, nickname, updatedBorderColor);
  }

  @Override
  public boolean equals(final Object obj) {

    if (obj instanceof CustomUserPropertiesDTO customUserPropertiesDTO) {

      return username.equals(customUserPropertiesDTO.username());
    }

    return false;
  }
}
