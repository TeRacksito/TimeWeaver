import { environment } from '../../environments/environment';

/**
 * Constructs the full API URL for a given route.
 * @param route it may start with a slash
 * @returns
 */
export function toApiUrl(route: string): string {
  return `${environment.apiUrl}${route}`;
}

export function getApiError(error: any, fallbackMessage: string) {
  const _error = error?.error || error;

  if (_error.message) {
    return _error.message;
  }

  if (_error != null && typeof _error === 'object') {
    const errorMessages = Object.entries(_error)
      .filter(([_, value]) => typeof value === 'string')
      .map(([key, value]) => `${key}: ${value}`)
      .join('\n');

    return errorMessages;
  }

  return fallbackMessage;
}
