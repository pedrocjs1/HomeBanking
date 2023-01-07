const {createApp} = Vue

const loan = createApp({
    data(){
        return {
            clientLoansTaken: [],
            cards:[],
            clientsAccount: {},
            loans: [],
            clientLoans: {},
            balanceTotalLoan: "",
            
            selectLoan: "",
            selectOwner:"asdasd",

            loanQuota: [],
            quotaSelect: undefined,
            accountTarget:"",
            accountTargetOther: "",

            description: "",

            accountOwn: [],

            username: "",
            

            checkedAccount: undefined,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            backgroundBody: "",
            backgroundTitle: "",
            shadowCard: "",
            bgGrey: "",
            backgroundLogo: "",

            checkedHeader: true,
            header: "",
            headerDesktop: "",

            balanceTotal: "",

            view: true,

            viewPassword: "d-none",
            noViewPassword: "",


            amount: 0,
            
            
            
            
        }
    },
    created(){
        this.loadData()
        this.checkedAccount = localStorage.getItem("mode") 
        
        
    },
    methods: {
        loadData(){
            axios
            .get("http://localhost:8080/api/clients/current")
            .then((data) =>{
                this.cards = data.data.cards.sort((a,b) => a.id - b.id)
                this.username = data.data.firstName + ' ' + data.data.lastName
                // this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                // this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc).toFixed(2)
                this.clientLoansTaken = data.data.loans
                this.accountOwn = data.data.accountDTO.sort((a,b) => a.id - b.id)
                console.log(this.accountOwn)
                this.accountOrigin = [... data.data.accountDTO.map(number => number.number)][0]
                this.accountTarget = data.data.accountDTO.length > 1 ? data.data.accountDTO[0].number : "No other account"
                console.log(this.client)
                
            })
            .catch((error) => console.log(error))

            axios.get("/api/loans")
            .then(data => {
                this.loans = data.data
                this.loans.sort((a, b) => a.id - b.id)
                this.selectLoan = this.loans[0].name
                
                console.log(this.loans)
                console.log(this.loanQuota)
                
            })
            .catch((error) => console.log(error))
            
            
        },
        collapseHeaderMovile(){
            if (this.header == "mob-menu-opened"){
                this.header = ""
            } else {
                this.header = "mob-menu-opened"
            }
        },
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        addCommas(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
        createCard() {
            axios
                    .post(
                        "/api/clients/current/cards",
                        `colorCard=${this.selectColor}&cardType=${this.selectType}`
                    )
                    .then((response) => window.location.href=("./cards.html"))

                    .catch((error) => Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: `${error.response.data}`,
                    }));
        },
        updateAmount(event) {
            this.amount = event.target.value;
        }, 
        loanApply() {

            let loanApplication = {
                id: this.loanQuota.id,
                amount: this.amount,
                payment: this.quotaSelect,
                targetAccount: this.accountTarget


            }

            Swal.fire({
                title: 'Do you apply loan?',
                showDenyButton: true,
                // showCancelButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {

                if (result.isConfirmed) {
                    if (this.amount > 0 ) {

                        axios.post('/api/loans', loanApplication)
                            .then(response => {
                                Swal.fire('Loan approved', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {

                                this.error = error.response.data
                                Swal.fire('Loan request Failed', this.error, 'error')
                                    .then(result => {
                                        window.location.reload()
                                })
                            })
                    } else {
                        Swal.fire('Transaction Failed', 'Complete all fields')
                                    .then(result => {
                                        window.location.reload()
                                    })

                    }
                    
                    
                    
                } else if (result.isDenied) {
                    Swal.fire('Cancel transaction', '', 'error')
                }
            })
    
        },
        hasLoan(type) {

              // Verificar si el cliente tiene un préstamo del tipo especificado
              const loansTaked = this.clientLoansTaken.find(loan => loan.name === type)
              if (loansTaked) {
                return true
              } else {
                return false
              }
        },
       
        
        
            
    },
    computed: {
      darkMode(){
        if (this.checkedAccount === "true" || this.checkedAccount === true) {
          this.currentText = "text-white"
          this.currentBackGround = "bg-dark"
          this.mode = "Dark Mode"
          this.backgroundBody = "backgroundBody"
          this.backgroundTitle = "bg-dark"
          this.shadowCard = "shadowWhite" 
          this.bgGrey = "bg-light borderRadius"
          this.backgroundLogo = ""
          localStorage.setItem("mode", true)

        } else if (this.checkedAccount === "false" || this.checkedAccount === false) {
          this.currentText = "text-black"
          this.currentBackGround = "backGroundGrey"
          this.mode = "Ligth Mode"
          this.boxShadow = "m-3 shadow bg-body rounded"
          this.backgroundBody = "backgroundWhite "
          this.backgroundTitle = "backgroundTitletarget"
          this.shadowCard = "shadow  bg-body rounded" 
          this.bgGrey = "backgroundBodyCard borderRadius"
          this.backgroundLogo = ""
          localStorage.setItem("mode", false)
        }
    },
    maxAmount() {
        const originAccount = this.loans.find(loan => loan.name === this.selectLoan);
        if (originAccount) {
          return originAccount.maxAmount;
        } else {
          return 0;
        }
    },
    originAccountAmount() {
        return `${this.amount}`;
    },
    loanQuotaMethod(){
        const loanQuotaa = this.loans.find(loan => loan.name === this.selectLoan)
        if (loanQuotaa === undefined) {
            this.loanQuota = 'Valor por defecto'
          } else {
            this.loanQuota = loanQuotaa
            this.quotaSelect = this.loanQuota.payments[0]

          }
        
    },
    imageUrl() {
        return `./images/${this.loanQuota.name}.jpeg`
      },

    
    
    
    }
})


loan.mount('#loan')
