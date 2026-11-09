export interface CreateOrderItemRequest {
  productId: number
  quantity: number
}

export interface CreateOrderRequest {
  customerName: string
  phoneNumber: string
  deliveryLocation: string
  notes: string
  merchantId: number
  items: CreateOrderItemRequest[]
}

export interface OrderItemResponse {
  productId: number
  productName: string
  quantity: number
  unitPrice: number | string
  subtotal: number | string
}

export interface OrderResponse {
  id: number
  orderNumber: string
  customerName: string
  phoneNumber: string
  deliveryLocation: string
  notes: string
  totalAmount: number | string
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED'
  createdAt: string
  merchantId: number
  items: OrderItemResponse[]
}

export interface InitiatePaymentRequest {
  orderId: number
  phoneNumber: string
}

export interface PaymentResponse {
  id: number
  orderId: number
  amount: number | string
  phoneNumber: string
  status: 'PENDING' | 'SUCCESS' | 'FAILED'
  mpesaReceiptNumber: string | null
  checkoutRequestId: string | null
  resultDesc: string | null
}

export const useOrderApi = () => {
  const config = useRuntimeConfig()
  const baseURL = config.public.apiBase as string

  const createOrder = (request: CreateOrderRequest) => {
    return $fetch<OrderResponse>('/api/orders', {
      baseURL,
      method: 'POST',
      body: request
    })
  }

  const initiatePayment = (request: InitiatePaymentRequest) => {
    return $fetch<PaymentResponse>('/api/payments/stk-push', {
      baseURL,
      method: 'POST',
      body: request
    })
  }

  const getPaymentsForOrder = (orderId: number) => {
    return $fetch<PaymentResponse[]>(`/api/payments/order/${orderId}`, {
      baseURL
    })
  }

  return {
    createOrder,
    initiatePayment,
    getPaymentsForOrder
  }
}
